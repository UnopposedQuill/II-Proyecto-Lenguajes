
from pyswip import Prolog

prolog = Prolog()

# Agregar elementos a la base de datos
prolog.assertz('ingrediente("Huevo")')
prolog.assertz('receta("Huevo Frito", "Desayuno")')
prolog.assertz('receta("Huevo Cocido", "Tentempié")')
prolog.assertz('ingredienteReceta("Huevo", "Huevo Frito")')
prolog.assertz('ingredienteReceta("Huevo", "Huevo Cocido")')
prolog.assertz('listaPasos(["Abrir el huevo", "Colocar Huevo en Sartén"], "Huevo Frito")')

# Intento de imprimir las recetas ----------------------------------------------------------------------------

# Un sitio donde guardar los resultados
listaRecetas = []

# Voy a hacer una consulta de todas las recetas

# Por cada solución
for solucion in prolog.query('receta(X, Y)'):
    # Agregar a los resultados
    listaRecetas.append([solucion['X'].decode(), solucion['Y'].decode()])

# Imprimir Resultados
print('Todas las recetas:')
for i in listaRecetas:
    print('\t', i[0], i[1])

# Ahora sólo las que tengan huevo como ingrediente ------------------------------------------------------------

# Agrego una regla que me retorna todas las recetas de la lista de conocimientos
prolog.assertz('recetas(Receta, Ingrediente):-' +
               'receta(Receta, _), ingrediente(Ingrediente), ingredienteReceta(Ingrediente, Receta)')
# prolog.consult('baseConocimientos.pl')

# Un sitio donde guardar los resultados
listaRecetas = []

# Por cada solución
for solucion in prolog.query('recetas(X, Y)'):
    # Agregar a los resultados
    listaRecetas.append(solucion['X'].decode())
    
# Imprimir Resultados
print('\nRecetas que tengan <Huevo> como ingrediente:')
for i in listaRecetas:
    print('\t', i)
