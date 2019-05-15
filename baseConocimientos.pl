
ingrediente("Huevo").
receta("Huevo Frito", "Desayuno").
receta("Huevo Cocido", "Tentempié").
ingredienteReceta("Huevo", "Huevo Frito").
ingredienteReceta("Huevo", "Huevo Cocido").
listaPasos(["Abrir el huevo", "Colocar Huevo en Sartén"], "Huevo Frito").


recetas(Receta, Ingrediente):- receta(Receta, _), ingrediente(Ingrediente), ingredienteReceta(Ingrediente, Receta).
