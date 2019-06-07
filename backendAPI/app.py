"""TODO:
  **Coneccion a sqlite/postgre -> login
  Aprender a escriir de manera decente
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
parserReceta.add_argument('ingredientes',action='append');
parserReceta.add_argument('pasos',action='append');
parserReceta.add_argument('imagen',action='append');
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
    #curl localhost:5000/recipe/info -dnombre="Name" -XGET
    """Retorna informacion de la  receta con el nombre
    { 'nombre' : nombreReceta,'tipo' : tipo,
      'ingrediente' : ['ing1','ing2',...],
      'pasos' : [paso1, paso2, ...] }
    """
    args = parserReceta.parse_args();
    if(args['nombre']==None): return{'Error':'nombre es campo requerido'};
    nombre = '"'+args['nombre']+'"';
    data = {}; envio = {'nombre':args['nombre']};
    PrologCallWRP('infoReceta('+nombre+',I,T,P,L).',['I','T','P','L']);
    existe=False;
    with open('data.json') as infile: data = json.load(infile);
    for receta in data:
      existe=True;
      print(receta);
      envio['ingrediente']=receta['I'];
      envio['pasos']=receta['P'];
      envio['tipo']=receta['T'];
      envio['imagenes']=receta['L'];
    if(existe): return envio;
    else: return {'Error':'Receta invalida'},404;

  def put(self):
    #curl localhost:5000/recipe/info -dnombre="nmbre" -dingrediente="ingreNuevo" -XPUT
    #curl localhost:5000/recipe/info -dnombre="nmbre" -dpasos="paso1" -dpasos="paso2" .. -dpasos="pasoN" -XPUT
    """modifica la informacion de la receta con el nombre"""
    """TODO: que no permita modificar recetas no existentes"""
    args = parserReceta.parse_args();
    if(args['nombre']==None): return {'message':'nombre es campo requrido'};
    nombre = args['nombre'];
    ingre = args['ingredientes'];
    pasos = args['pasos'];
    img = args['imagen'];
    
    """Check si la receta existe"""
    existe = False;
    PrologCallWRP('receta("'+nombre+'",X).',['X']);
    with open('data.json') as infile:
      data = json.load(infile);
      for receta in data:
        if receta:
          existe = True;
    if not existe:
      return {'Error':'Receta no existente'};
    if(ingre!=None):
      for ing in ingre:
        print(ing);
        PrologCallWRP('escribirClausula(ingrediente("'+ing+'")).',[]);
        PrologCallWRP('escribirClausula(ingredienteReceta("'+ing+'","'+nombre+'")).',[]);
    if(pasos!=None):
      lista = '["'+'","'.join(pasos)+'"]'
      PrologCallWRP('escribirClausula(listaPasos('+lista+',"'+nombre+'")).',[])
    if(img):
      for im in img: 
        print(im);
        PrologCallWRP('escribirClausula(listaImagenes("'+im+'","'+nombre+'")).',[]);
    return {'Message':'Receta modificada existosamente'};

  def post(self):
    #curl localhost:5000/recipe/info -dnombre="nmbre" -dtipo="Tipo" -XPOST
    args = parserReceta.parse_args();
    if(args['nombre']==None or args['tipo']==None):
      return {'message':'nombre y tipo son requeridos'},891;
    nombre = args['nombre']
    """Check si la receta existe"""
    existe = False;
    PrologCallWRP('receta("'+nombre+'",X).',['X']);
    with open('data.json') as infile:
      data = json.load(infile);
      for receta in data:
        if receta:
          existe = True;
    if existe:
      return {'Error':'Receta ya existente'},833;
    tipo = args['tipo']
    PrologCallWRP('escribirClausula(receta("'+nombre+'","'+tipo+'")).',[])
    return {nombre:tipo}
"""----------------------------------------------------------------------------"""

"""----------------------------------------------------------------------------"""
class Recipes(Resource):
  def get(self): 
    #curl localhost:5000/recipe -XGET
    """Retorna una lista de todas las recetas"""
    PrologCallWRP('receta(X,Y)',['X','Y'])
    data = {}; envio = [];
    with open('data.json') as infile: data = json.load(infile)
    for receta in data: envio.append(receta['X']);
    return {'recetas':envio};
"""----------------------------------------------------------------------------"""


"""----------------------------------------------------------------------------"""
class Filter(Resource):
  def get(self):
    args = parserReceta.parse_args();
    request='recetas(';
    query=[];
    if(args['nombre']):
      request=request+'"'+args['nombre']+'",';
    else:
      request=request+'R,';
      query+=['R'];
    if(args['ingrediente']):
      request=request+'"'+args['ingrediente']+'",';
    else:
      request=request+'I,';
      query+=['I'];
    if(args['tipo']):
      request=request+'"'+args['tipo']+'").';
    else:
      request=request+'T).';
      query+=['T'];
    PrologCallWRP(request,query);
    data = {}; envio=[];
    with open('data.json') as infile: data=json.load(infile);
    for receta in data:
      print(receta);
      nombre = ""
      if(not args['nombre']): nombre = receta['R'];
      else: nombre = args['nombre'];
      envio.append(nombre) if nombre not in envio else envio;
    return {'result':envio};
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
api.add_resource(Filter,'/recipe/filter')
api.add_resource(Login,'/login')

if __name__ == '__main__':
    app.run(debug=True)
