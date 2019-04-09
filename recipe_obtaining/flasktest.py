# -*- coding: utf-8 -*-

from flask import Flask, request, jsonify
from recipe_module import get_recipe

app = Flask(__name__)

@app.route('/', methods=['GET'])
def init():
    return 'Active'


@app.route('/recipe/<plate>', methods=['GET'])
def hello(plate):
    #return jsonify({'ip':'88.0.109.140','msg':'Welcome to Salk API','status':True, 'neural-network':connected})
    return str(get_recipe(plate))

if __name__ == '__main__':
    app.run(debug=False, host='0.0.0.0', port=5555) 
	  