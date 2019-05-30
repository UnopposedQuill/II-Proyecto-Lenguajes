"""TODO:
  Impleentar el get de recetas y receta independiente
  **Coneccion a sqlite/postgre -> login
  Aprender a escriir de manera decente
  Conectar al prolog usando lode Paul
"""
import json
import sys
import subprocess
from flask import Flask
from flask_restful import Resource, Api, reqparse

def PrologCallWRP(query,args): print(query); subprocess.call(['python3','execProlog.py',query]+args); return;

parserReceta = reqparse.RequestParser(); #parser del input
#los campos posibles y esperados, cada uno es opcional
parserReceta.add_argument('nombre');
parserReceta.add_argument('tipo');
parserReceta.add_argument('ingrediente');
parserReceta.add_argument('pasos',action='append');
parserReceta.add_argument('token',required=True);

#parser para la informacion del login
parserLogin = reqparse.RequestParser();
parserLogin.add_argument('user',required=True);
parserLogin.add_argument('pass',required=True);

app = Flask(__name__)
api = Api(app)

"""----------------------------------------------------------------------------"""
class Recipe(Resource):
  def get(self):
    """Retorna informacion de la  receta con el nombre
    { 'nombre' : nombreReceta,'tipo' : tipo,
      'ingrediente' : ['ing1','ing2',...],
      'pasos' : [paso1, paso2, ...] }
    """
    args = parserReceta.parse_args();
    if(args['nombre']==None): return{'Error':'nombre es campo requerido'};
    nombre = '"'+args['nombre']+'"';
    data = {}; envio = {'nombre':nombre,'ingrediente':[],'paso':[]};
    ###
    PrologCallWRP('receta('+nombre+',X)',['X']);
    with open('data.json') as infile: data = json.load(infile);
    for receta in data: envio['tipo']=receta['X'];
    ###
    PrologCallWRP('ingredienteReceta(X,'+nombre+')',['X']);
    with open('data.json') as infile: data = json.load(infile);
    for receta in data: envio['ingrediente'].append(receta['X']);
    ###
    PrologCallWRP('listaPasos(X,'+nombre+')',['X']);
    with open('data.json') as infile: data = json.load(infile);
    for receta in data: envio['paso']=receta['X'];
    ###
    return envio

  def put(self):
    """modifica la informacion de la receta con el nombre"""
    args = parserReceta.parse_args();
    if(args['nombre']==None): return {'message':'nombre es campo requrido'};
    nombre = args['nombre'];
    ingre = args['ingrediente'];
    pasos = args['pasos'];
    if(ingre!=None):
      PrologCallWRP('escribirClausula(ingrediente("'+ingre+'")).',[]);
      PrologCallWRP('escribirClausula(ingredienteReceta("'+ingre+'","'+nombre+'")).',[]);
    if(pasos!=None):
      lista = '["'+'","'.join(pasos)+'"]'
      PrologCallWRP('escribirClausula(listaPasos('+lista+',"'+nombre+'"))',[])
    return {}

  def post(self):
    args = parserReceta.parse_args();
    if(args['nombre']==None or args['tipo']==None):
      return {'message':'nombre y tipo son requeridos'},891;
    nombre = args['nombre']
    tipo = args['tipo']
    PrologCallWRP('escribirClausula(receta("'+nombre+'","'+tipo+'")).',[])
    return {nombre:tipo}
"""----------------------------------------------------------------------------"""

"""----------------------------------------------------------------------------"""
class Recipes(Resource):
  def get(self):
    """Retorna una lista de recetas"""
    PrologCallWRP('receta(X,Y)',['X','Y'])
    data = {}; envio = [];
    with open('data.json') as infile: data = json.load(infile)
    for receta in data: envio.append({'nombre' : receta['X'],'tipo': receta['Y']});
    return {'receta':envio};

  def post(self):
    return {};
"""----------------------------------------------------------------------------"""

"""----------------------------------------------------------------------------"""
class Login(Resource):
  """el data esperado para estos es: {'user':Username,'pass':PaswsOrd}"""
  def get(self):
    """retorna el token para un username|password existente, o eror en otro caso"""
    args = parserLogin.parse_args();
    return {'I dont know you':'And I dont care to know you'};
  def post(self):
    """crea un nuevo username|pass en la base, y retorna el token asignado"""
    args = parserLogin.parse_args();
    return {'I dont know you':'And I dont care to know you'};
"""----------------------------------------------------------------------------"""

"""----------------------------------------------------------------------------"""
class HelloWorld(Resource):
    def get(self):
        return {'hello': 'world'}
"""----------------------------------------------------------------------------"""

api.add_resource(HelloWorld, '/')
api.add_resource(Recipes,'/recipe')
api.add_resource(Recipe,'/recipe/info')
#api.add_resource(Recipe,'/recipe/filter')
api.add_resource(Login,'/login')

if __name__ == '__main__':
    app.run(debug=True)
