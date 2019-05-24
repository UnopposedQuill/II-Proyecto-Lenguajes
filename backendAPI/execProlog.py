"""
uso:
python execProlog.py 'consulta(Var1,Var2,"String")' 'Var1' 'Var2'
Ej:
python execProlog.py 'receta(X,Y)' 'X' 'Y'
python execProlog.py 'receta(X,"Desayuno")' 'X'
"""
import json
import sys
from pyswip import Prolog

if __name__=="__main__":
  prologui = Prolog();
  prologui.consult('reglas.pl');
  prologui.consult('baseConocimientos.pl')
  with open('data.json','w') as outfile:
    data = []
    for ele in list(prologui.query(sys.argv[1])):
      loc = {};
      for it in range(2,len(sys.argv)):
        loc[sys.argv[it]] = ele[sys.argv[it]].decode('utf-8','replace')
      data.append(loc);
    json.dump(data,outfile)
