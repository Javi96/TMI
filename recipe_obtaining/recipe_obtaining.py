# -*- coding: utf-8 -*-

from flask import Flask
from recipe_module import get_recipe
#import datetime

app = Flask(__name__)


#Método para ver si la API está activada.
@app.route('/', methods=['GET'])
def init():
    return 'API state: Active \n To ask for a recipe send a GET msg to /recipe/<plate_name>'


#Devolverá un objeto tipo bytes. Este, codificará un string con forma de JSON. 
#Se devuelve de esta forma para que pueda operarse el resultado independientemente del lenguaje del programa que llama a la API.
@app.route('/recipe/<plate>', methods=['GET'])
def recipe_obtaining(plate):
        
    #print(datetime.datetime.now()+'------------- Solicitada receta para plato: '+ plate)   
    print('-Solicitada receta para plato: '+ str(plate))
    return str(get_recipe(plate))


if __name__ == '__main__':
    app.run(debug=False, host='0.0.0.0', port=5555) 
	  