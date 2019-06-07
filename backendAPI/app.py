"""TODO:
  **Coneccion a sqlite/postgre -> login
  Aprender a escriir de manera decente
"""
import random
import string
import os
import psycopg2
import json
import sys
import subprocess
from flask import Flask
from flask_restful import Resource, Api, reqparse

DATABASE_URL = os.environ['DATABASE_URL']
#DATABASE_URL = "postgres://gjdfftstillhri:f526e32b849bf31e858fd0ffb90ec477edb42888f3f4a3529165779c6cb7111e@ec2-50-19-114-27.compute-1.amazonaws.com:5432/dfeqo4r483r9kf"

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

def hash(sttt):
  s=0;
  for lt in sttt:
    s+=ord(lt)-ord('a');
    s*=27;
  return s%11;

def CheckToken(token):
  if(hash(token)>0): return False;
  ret = False;
  conn = psycopg2.connect(DATABASE_URL, sslmode='require')
  cur=conn.cursor();
  cur.execute('select * from Users where "token"=%s',(token,));
  if(cur.rowcount>0):
    ret = True;
  cur.close();
  conn.close();
  return ret;

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
    if( not CheckToken(args['token']) ): return {'Error':'token invalido'};
    if(args['nombre']==None): return{'Error':'nombre es campo requerido'},401;
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
    if(existe): return envio,200;
    else: return {'Error':'Receta invalida'},404;

  def put(self):
    #curl localhost:5000/recipe/info -dnombre="nmbre" -dingrediente="ingreNuevo" -XPUT
    #curl localhost:5000/recipe/info -dnombre="nmbre" -dpasos="paso1" -dpasos="paso2" .. -dpasos="pasoN" -XPUT
    """modifica la informacion de la receta con el nombre"""
    """TODO: que no permita modificar recetas no existentes"""
    args = parserReceta.parse_args();
    if( not CheckToken(args['token']) ): return {'Error':'token invalido'},401;
    if(args['nombre']==None): return {'message':'nombre es campo requrido'},401;
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
      return {'Error':'Receta no existente'},401;
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
    return {'Message':'Receta modificada existosamente'},200;

  def post(self):
    #curl localhost:5000/recipe/info -dnombre="nmbre" -dtipo="Tipo" -XPOST
    args = parserReceta.parse_args();
    if( not CheckToken(args['token']) ): return {'Error':'token invalido'},401;
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
    return {nombre:tipo},200
"""----------------------------------------------------------------------------"""

"""----------------------------------------------------------------------------"""
class Recipes(Resource):
  def get(self): 
    #curl localhost:5000/recipe -XGET
    """Retorna una lista de todas las recetas"""
    args = parserReceta.parse_args();
    if( not CheckToken(args['token']) ): return {'Error':'token invalido'},401;
    PrologCallWRP('receta(X,Y)',['X','Y'])
    data = {}; envio = [];
    with open('data.json') as infile: data = json.load(infile)
    for receta in data: envio.append(receta['X']);
    return {'recetas':envio},200;
"""----------------------------------------------------------------------------"""


"""----------------------------------------------------------------------------"""
class Filter(Resource):
  def get(self):
    args = parserReceta.parse_args();
    if( not CheckToken(args['token']) ): return {'Error':'token invalido'},401;
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
    return {'result':envio},200;
"""----------------------------------------------------------------------------"""

"""----------------------------------------------------------------------------"""
class Login(Resource):
  """el data esperado para estos es: {'user':Username,'pass':PaswsOrd}"""
  def get(self):
    """retorna el token para un username|password existente, o eror en otro caso"""
    args = parserLogin.parse_args();
    conn = psycopg2.connect(DATABASE_URL, sslmode='require')
    cur = conn.cursor();
    token = ''.join(random.choices(string.ascii_lowercase, k=29));
    token = token + chr(11-hash(token)+ord('a'));
    cur.execute('update users set "token"=%s where "usuario"=%s and "passwd"=%s',(token,args['user'],args['pass']));
    conn.commit();
    cur.execute('select token from users where "usuario"=%s and "passwd"=%s',(args['user'],args['pass']));
    if(cur.rowcount==0):
      cur.close();
      conn.close();
      return {'I dont know you':'And I dont care to know you'},404;
    else:
      token = cur.fetchone()[0];
      cur.close();
      conn.close();
      return {'token':token},200;
  
  def put(self):
    args = parserReceta.parse_args();
    if( not CheckToken(args['token'])):
      return {'Error': 'Token invalido'};
    conn = psycopg2.connect(DATABASE_URL, sslmode='require')
    cur = conn.cursor();
    cur.execute('update users set "token" = %s where "token"=%s',("none",args['token']));
    conn.commit();
    cur.close();
    conn.close();
    return {'I dont know you':'And I dont care to know you'},200;

  def post(self):
    """crea un nuevo username|pass en la base, y retorna el token asignado"""
    args = parserLogin.parse_args();
    conn = psycopg2.connect(DATABASE_URL, sslmode='require')
    cur=conn.cursor();
    cur.execute('select token from Users where "usuario"=%s',(args['user'],));
    if(cur.rowcount>0):
      cur.close();
      conn.close();
      return {'Error':'Cuenta ya existe'},456;
    else:
      token = ''.join(random.choices(string.ascii_lowercase, k=29));
      token = token + chr(11-hash(token)+ord('a'))
      cur.execute('insert into Users values(%s,%s,%s)',(args['user'],args['pass'],token));
      conn.commit();
      cur.close();
      conn.close();
      return {'token':token},201;
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
