import flask
from flask import Flask, jsonify
import werkzeug

api = Flask(__name__)

serverState = {"ServerState":"ServerUP"}


@api.route('/serverState', methods=['GET'])
def get_serverState():
  print("\n")
  return jsonify(serverState)
  
@api.route('/uploadImage', methods = ['GET', 'POST'])
def image_handle_request():
    imagefile = flask.request.files['image']
    filename = werkzeug.utils.secure_filename(imagefile.filename)
    print("\nReceived image file\nFile name : " + imagefile.filename)
    imagefile.save(filename)
    return "Image Uploaded Successfully" 
    
@api.route('/uploadCvs', methods = ['GET', 'POST'])
def csv_handle_request():
    imagefile = flask.request.files['csv']
    filename = werkzeug.utils.secure_filename(imagefile.filename)
    print("\nReceived csv file\nFile name : " + imagefile.filename)
    imagefile.save(filename)
    return "Cvs Uploaded Successfully" 


if __name__ == '__main__':
    api.run(host='0.0.0.0',port=5000, threaded=True, debug=True)