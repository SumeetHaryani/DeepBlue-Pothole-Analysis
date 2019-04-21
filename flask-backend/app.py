from flask import Flask, render_template, redirect, url_for,flash, request
from flask.json import jsonify
from werkzeug.utils import secure_filename
import os
import pandas as pd
import sys
import random
import math
import re
import time
import numpy as np
import tensorflow as tf
import matplotlib
import matplotlib.pyplot as plt
import matplotlib.patches as patches
from pathlib import Path
import pyrebase
from mrcnn import utils
from mrcnn import visualize
from mrcnn.visualize import display_images
import mrcnn.model as modellib
from mrcnn.model import log
import pothole
import exifread
from PIL import Image
from PIL.ExifTags import TAGS
import requests
import cv2
import urllib
import datetime
from geopy.geocoders import Nominatim
ROOT_DIR = Path().absolute()
sys.path.append(ROOT_DIR)

app = Flask(__name__)
app.config['SECRET_KEY'] = 'waduhek'
MODEL_DIR = os.path.join(ROOT_DIR, "logs")
POTHOLE_DIR = os.path.join(ROOT_DIR, "uploads/")
POTHOLE_WEIGHTS_PATH =  os.path.join(MODEL_DIR,'weights.h5') # TODO: update this path

DEVICE = "/gpu:0"  
TEST_MODE = "inference"
pothole_a = None

config = {
    'apiKey': "AIzaSyDSPHnMAGY1iqe12wk_WRakxCoEutxM7dE",
    'authDomain': "deep-blue-sample.firebaseapp.com",
    'databaseURL': "https://deep-blue-sample.firebaseio.com",
    'projectId': "deep-blue-sample",
    'storageBucket': "deep-blue-sample.appspot.com",
    'messagingSenderId': "1070942423558"
  }

firebase = pyrebase.initialize_app(config)
storage = firebase.storage()
db = firebase.database()

def get_model():
    config = pothole.PotholeConfig()
    class InferenceConfig(config.__class__):
        # Run detection on one image at a time
        GPU_COUNT = 1
        IMAGES_PER_GPU = 1
    config = InferenceConfig()
    with tf.device(DEVICE):
        global model
        model = modellib.MaskRCNN(mode=TEST_MODE, model_dir=MODEL_DIR,config=config)
    print("Loading weights ", POTHOLE_WEIGHTS_PATH)
    model.load_weights(POTHOLE_WEIGHTS_PATH, by_name=True)

    print('**** weights loaded')

print('**** Loading keras model')
get_model()
print('**** model loaded')


@app.route('/')
def index():
    print(ROOT_DIR)
    print(MODEL_DIR)
    print(POTHOLE_WEIGHTS_PATH)
    return 'hello'

def get_exif(fn):
    ret = {}
    i = Image.open(fn)
    info = i._getexif()
    for tag, value in info.items():
        decoded = TAGS.get(tag, tag)
        ret[decoded] = value
    return ret

# Function to perform the analysis for area
def save_pothole(filename, focal_length, sensor_width, sensor_height, dist_from_ground):   

    image = cv2.imread(POTHOLE_DIR + filename , cv2.IMREAD_COLOR)

    print(image.shape)
    px = image.shape[1]
    py = image.shape[0]

    gsd =[(dist_from_ground*sensor_height/(focal_length*py)), (dist_from_ground*sensor_width/(focal_length*px))]
    fgsd = max(gsd)
    actual_x = fgsd * px
    actual_y = fgsd * py
    total_grnd_area = actual_x*actual_y
    results = model.detect([image], verbose=1)
    
    _, ax = get_ax(1)
    r = results[0]
    ret = visualize.display_instances(filename,image, r['rois'], r['masks'], r['class_ids'], 
                                ['BG','pothole'], r['scores'], ax=ax,
                                title="Predictions")

    visualize.save_image(image, filename,r['rois'], r['masks'], r['class_ids'],r['scores'],['BG','pothole'])
    print(ret)
    print('-----------------------------------------')
    
    #TODO : Calculate individual area for each pothole.
    
    if ret:
        print(r['rois'])
        print('-----------------------------------------')
        print(r['masks'].shape)
        mask_img = r['masks'][:,:,0]
        print(type(mask_img))
        print('-----------------------------------------')


        area_in_pixel = np.reshape(r['masks'], (-1, r['masks'].shape[-1])).astype(np.float32).sum()
        perc_area_covered_pothole = area_in_pixel/(px*py)
        pothole_area = perc_area_covered_pothole * total_grnd_area
        return (focal_length,sensor_width,sensor_height,dist_from_ground,pothole_area,perc_area_covered_pothole*100, len(r['class_ids']))
    else:
        return ret
def get_ax(rows=1, cols=1, size=16):
    fig, ax = plt.subplots(rows, cols, figsize=(size*cols, size*rows))
    return (fig,ax)

def _get_if_exist(data, key):
    if key in data:
        return data[key]

    return None

def _convert_to_degress(value):
    """
    Helper function to convert the GPS coordinates stored in the EXIF to degress in float format
    :param value:
    :type value: exifread.utils.Ratio
    :rtype: float
    """
    d = float(value.values[0].num) / float(value.values[0].den)
    m = float(value.values[1].num) / float(value.values[1].den)
    s = float(value.values[2].num) / float(value.values[2].den)

    return d + (m / 60.0) + (s / 3600.0)
    
def get_exif_location(exif_data):
    """
    Returns the latitude and longitude, if available, from the provided exif_data (obtained through get_exif_data above)
    """
    lat = None
    lon = None

    gps_latitude = _get_if_exist(exif_data, 'GPS GPSLatitude')
    gps_latitude_ref = _get_if_exist(exif_data, 'GPS GPSLatitudeRef')
    gps_longitude = _get_if_exist(exif_data, 'GPS GPSLongitude')
    gps_longitude_ref = _get_if_exist(exif_data, 'GPS GPSLongitudeRef')

    if gps_latitude and gps_latitude_ref and gps_longitude and gps_longitude_ref:
        lat = _convert_to_degress(gps_latitude)
        if gps_latitude_ref.values[0] != 'N':
            lat = 0 - lat

        lon = _convert_to_degress(gps_longitude)
        if gps_longitude_ref.values[0] != 'E':
            lon = 0 - lon

    return lat, lon

def get_exif_data(image_file):
    with open(image_file, 'rb') as f:
        exif_tags = exifread.process_file(f)
    return exif_tags 

@app.route('/getjson/<fire>', methods=['GET','POST'])
def getjson(fire):

    uid = request.args.get('uid')
    height = request.args.get('height')
    user_name = request.args.get('name')
    user_email = request.args.get('email')
    userinfo = db.child("userinfo/"+uid).get()
    mobile_info = userinfo.val()
    print (uid, height, mobile_info)
    print('height here ---', height)
    url = storage.child("Photos/"+fire).get_url(None)
    storage.child("Photos/"+fire).download("uploads/test_"+fire)
    lati, longi = get_exif_location(get_exif_data("uploads/test_"+fire))
    print(lati, longi)

    geolocator = Nominatim(user_agent="app.py")
    location = geolocator.reverse(f'{lati}, {longi}')
    location_add = location.address

    z = save_pothole('test_'+fire,focal_length=float(mobile_info['focal_length']), sensor_height = float(mobile_info['sensor_height']), 
                        sensor_width=float(mobile_info['sensor_width']), dist_from_ground=float(height))
    
    date = '{0:%Y-%m-%d %H:%M:%S}'.format(datetime.datetime.now())
    if z:
        print(z)
        storage.child("results/" + fire).put('static/test_'+fire +'.png')
        url = storage.child("results/" + fire).get_url(None)
        data =  {
            'uid':uid,
            'user_name':user_name,
            'user_email': user_email,
            'pothole_name' : fire,
            'pothole_area' : z[4],
            'date'  : date,
            'pic_url' : url,
            'number_of_potholes' : z[6],
            'latitude' : lati,
            'longitude' : longi,
            'location_add' : location_add,
            'status': 'Unattended'
        }

        location = {
            'uid':uid,
            'pothole_name' : fire,
            'latitude' : lati,
            'longitude' : longi
        }

        # print(data)

        #TODO : Delete the download and analyzed images.
        db.child('result/' + uid).push(data)
        db.child('locations/').push(location)

        return ('Pothole Successfully analyzed...' + 'Area = ' + str(z[4]))
    else :
        return ('Pothole not found')

if __name__ == "__main__":
    app.run(debug=True, port=5000)
