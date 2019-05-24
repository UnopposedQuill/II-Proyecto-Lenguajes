"""TODO:
  Impleentar el get de recetas y receta independiente
  **Coneccion a sqlite/postgre -> login
  Aprender a escriir de manera decente
  Conectar al prolog usando lode Paul
"""
from flask import Flask
from flask_restful import Resource, Api, reqparse

parserReceta = reqparse.RequestParser(); #parser del input
#los campos posibles y esperados, cada uno es opcional
parserReceta.add_argument('nombre');
parserReceta.add_argument('tipo');
parserReceta.add_argument('ingrediente');
parserReceta.add_argument('token',required=True);

#parser para la informacion del login
parserLogin = reqparse.RequestParser();
parserLogin.add_argument('user',required=True);
parserLogin.add_argument('pass',required=True);

app = Flask(__name__)
api = Api(app)

class Recipe(Resource):
  def get(self,nombre):
    """Retorna informacion de la  receta con el nombre
    {
      'nombre' : nombreReceta,
      'tipo' : tipo,
      'ingrediente' : ['ing1','ing2',...],
      'pasos' : [paso1, paso2, ...]
    }
    """
    return {
      'nombre':nombre,
      'tipo':'Blin-Suka',
      'ingrediente':[
        'Leche',
        'Chicken Produce',
        'Azuca',
        'algolr ma'
      ],
      'pasos' : [
        '1. Make blin, blyat',
        '2. Enyoy',
        '3. ggo to gulag'
      ]
    };

  def put(self,nombre):
    """modifica la informacion de la receta con el nombre"""
    return {}

  def delete(self,nombre):
    """elimina la receta con el nombre"""
    return {}

class Recipes(Resource):
  def get(self):
    """Retorna un json con una lista de recetas"""
    """
    { 
      'receta1" : {'nombre' : Nombre, 'tipo' : Tipo},
      'receta2" : {'nombre' : Nombre, 'tipo' : Tipo},
      'receta3" : {'nombre' : Nombre, 'tipo' : Tipo}
    }
    """
    """se aceptan sugerencias"""
    return {'wip':'todas las recetas'};

  def post(self):
    """Aca crea una nueva receta con la informacion dada"""
    """
      Obligatorio recibir el nombre y tipo, una lista de ingredientes y pasos es opcional
    """
    args = parserReceta.parse_args(); #dic con los campos
    if(args['nombre']==None or args['tipo']==None):
      return {'Error':'nombre y tipo son campos requeridos'},891;
    else:
      print('nombre = '+args['nombre']);
      print('tipo = '  +args['tipo']  );
      return {
              'nombre': args['nombre'],
              'tipo'  : args['tipo']
             };

class Login(Resource):
  """el data esperado para estos es: {'user':Username,'pass':PaswsOrd}"""
  def get(self):
    """retorna el token para un username|password existente, o eror en otro caso"""
    args = parserLogin.parse_args();
    return {'I dont know you':'And I dont care to know you'};

  def put(self):
    """modifica un user|pass existente dado un token valido, ni se para que pongo esot"""
    args = parserLogin.parse_args();
    return {'I dont know you':'And I dont care to know you'};

  def post(self):
    """crea un nuevo username|pass en la base, y retorna el token asignado"""
    args = parserLogin.parse_args();
    return {'I dont know you':'And I dont care to know you'};

class HelloWorld(Resource):
    def get(self):
        return {'hello': 'world'}

api.add_resource(HelloWorld, '/')
api.add_resource(Recipes,'/recipe')
api.add_resource(Recipe,'/recipe/<string:nombre>')
api.add_resource(Login,'/login')

if __name__ == '__main__':
    app.run(debug=True)
