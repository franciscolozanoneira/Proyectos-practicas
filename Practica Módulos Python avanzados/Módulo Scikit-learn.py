# Esta es la librería principal que necesito para que el programa "piense".
# En lugar de escribir yo miles de reglas,
# importo el RandomForestClassifier de sklearn.
# Es básicamente el motor de la IA que me permite crear un conjunto de personas
# que va a analizar los patrones de los jugadores automáticamente.
from sklearn.ensemble import RandomForestClassifier
# Uso numpy solo para organizar los números en listas y generar los datos aleatorios
import numpy as np

# Datos para entrenamiento (45 Perfiles)
# 6 Métricas: [Goles, Asistencias, Recuperaciones, Pases, KM, Partidos de liga]
datos = [
    # Clase 2: Futura Leyenda
    [50, 15, 5, 2100, 10.2, 37],  # Futuro Messi
    [48, 12, 8, 1200, 11.0, 38],  # Futuro Cristiano
    [15, 28, 12, 1600, 10.5, 34],  # Futuro Ronaldinho
    [8, 10, 85, 1100, 10.8, 36],  # Futuro Puyol/Ramos
    [2, 35, 40, 2600, 12.0, 38],  # Futuro Xavi/Modric
    [4, 25, 35, 2400, 11.8, 35],  # Futuro Kroos/Xabi Alonso
    [1, 5, 95, 1400, 12.5, 34],  # Futuro Mascherano
    [12, 25, 20, 1800, 13.0, 36],  # Futuro Iniesta
    [3, 30, 25, 2300, 9.5, 35],  # PERFIL PIRLO
    [30, 8, 10, 500, 10.0, 34],  # PERFIL HAALAND/MBAPPÉ
    [2, 8, 70, 1150, 10.9, 35],  # Central muy bueno
    [1, 15, 60, 1400, 13.5, 37],  # Lateral tipo Cafú/Roberto Carlos

    # CLASE 1: Interesantes
    [12, 5, 2, 350, 9.5, 25],
    [6, 10, 18, 650, 10.8, 22],
    [2, 4, 38, 700, 10.2, 24],
    [1, 8, 32, 800, 11.5, 20],
    [8, 8, 10, 500, 10.0, 28],
    [4, 12, 25, 950, 12.0, 26],
    [0, 2, 45, 600, 9.8, 22],
    [10, 3, 5, 300, 9.2, 18],
    [3, 7, 20, 850, 11.2, 21],
    [1, 5, 40, 750, 10.6, 23],
    [5, 5, 25, 600, 10.0, 15],
    [2, 6, 15, 500, 11.0, 12],
    [0, 3, 30, 450, 10.5, 14],
    [7, 2, 10, 300, 9.0, 10],
    [1, 1, 35, 550, 10.2, 13],

    # CLASE 0: Descartes
    [2, 1, 5, 180, 8.2, 8],
    [0, 2, 12, 250, 7.5, 10],
    [1, 0, 15, 300, 8.0, 12],
    [0, 0, 4, 80, 7.0, 4],
    [3, 1, 2, 150, 8.5, 7],
    [0, 1, 10, 200, 7.8, 5],
    [1, 0, 8, 120, 8.1, 6],
    [0, 0, 20, 100, 7.2, 9],
    [2, 0, 1, 50, 7.0, 3],
    [1, 1, 1, 100, 6.0, 2],
    [0, 0, 5, 50, 5.0, 1],
    [0, 0, 0, 20, 2.0, 1],
    [0, 0, 10, 80, 7.0, 4],
    [1, 0, 0, 40, 6.5, 2],
    [0, 0, 2, 10, 1.0, 1],
    [0, 1, 0, 30, 3.5, 2],
    [1, 0, 1, 20, 4.0, 1],
    [0, 0, 0, 0, 0, 0]
]
# Aquí le decimos a nuestro modelo_ia qué es cada cosa para que aprenda a diferenciar los patrones.
resultados = [
    2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, # CLASE 2: La clase que buscamos jugadores que se parezcan a estos.
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, # CLASE 1: El término medio. No son prioridad pero se ven por si acaso.
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 # CLASE 0: El filtro. Lo que la IA debe aprender a rechazar.
]
# RandomForest: montas una reunión con 100 ojeqadores.
# Cada uno se fija en una cosa: uno en los goles, otro en los km, etc
# Al final, el fichaje se decide por lo que diga la mayoría.
#  Si un ojeador tiene un mal día, los otros 99 lo corrigen.
modelo_ia = RandomForestClassifier(n_estimators=100)

# .fit (El entrenamiento): Aquí es donde los 100 expertos estudian los datos uniendolos con los resultados
# que les facilitamos de jugadores antiguos. Les enseñamos qué 'nota' sacó cada uno.
# Despues el modelo_ia ya está listo para analizar el mercado y encontrar los mejores fichajes.
modelo_ia.fit(datos, resultados)


# Funcion que sirve para meter en el sistema a cualquier jugador que hayamos visto nosotros mismos,
# sin esperar a que el programa lo genere al azar
def AñadirJugador(nombres, datos, nombre, estadisticas):
    """
    nombre: Texto (Ej: "Lamine Yamal")
    estadisticas: Lista [Goles, Asistencias, Recuperaciones, Pases, KM, Partidos de Liga ]
    """
    # Le ponemos una etiqueta para saber que no es una promesa aleatoria,
    # sino un fichaje que nosotros hemos propuesto manualmente.
    nombres.append("Ojeado Manual: " + nombre)
    datos.append(estadisticas)


# Generacion automatica del mercado de fichajes de 40 candidatos (Sin porteros)
Id_candidatos = []
ListaPuntos = []

for i in range(1, 41):
    Id_candidatos.append("Id_Promesa: " + str(i))
    # Generamos los datos uno a uno
    goles = np.random.randint(0, 55)
    asistencias = np.random.randint(0, 40)
    recuperaciones = np.random.randint(0, 100)
    pases = np.random.randint(100, 2900)
    km = round(np.random.uniform(7.5, 14.5), 1)
    pj = np.random.randint(1, 38)
    ListaPuntos.append([goles, asistencias, recuperaciones, pases, km, pj])

# Creamos a los ejemplos manuales
AñadirJugador(Id_candidatos, ListaPuntos, "Lamine Yamal", [15, 22, 18, 1400, 11.2, 35])
AñadirJugador(Id_candidatos, ListaPuntos, "Rodri", [8, 12, 75, 2600, 12.5, 37])

# La IA analiza a los candidatos y le asigna una probabilidad de que su fichaje sea bueno
probabilidades = modelo_ia.predict_proba(ListaPuntos)
Informe = []

# Analisis de los datos para poder asignarles un rol dependiendod de sus datos
for i in range(len(Id_candidatos)):
    # Extraemos los datos para que sea mas visual a la hora de leer que es que
    goles = ListaPuntos[i][0]
    asist = ListaPuntos[i][1]
    recup = ListaPuntos[i][2]
    pases = ListaPuntos[i][3]
    km    = ListaPuntos[i][4]

    #  Roles especificos
    #  Defensas
    if recup > 50:
        if pases > 1300:
            rol_detectado = "CEN" # Central
        elif km > 11.5:
            rol_detectado = "LAT" # Lateral
        else:
            rol_detectado = "CEN" # Central

    #  Centrocampistas
    elif pases > 1200:
        if recup > 40:
            rol_detectado = "MCD" # Medio centro defensivo
        elif asist > 18:
            rol_detectado = "MCO" # Mediapunta ofensivo
        elif km > 12:
            rol_detectado = "MC"  # Medio centro
        else:
            rol_detectado = "MC"

    #  Extremos
    elif km > 11 and asist > 12:
        if pases > 800:
            rol_detectado = "MI" if i % 2 == 0 else "MD" # Mediocentros Izq/Der
        else:
            rol_detectado = "EI" if i % 2 == 0 else "ED" # Extremos

    #  Delanteros
    else:
        rol_detectado = "DEL"

    # Significado de probabilidades[i][2]:
    # El [2] le dice al código que solo nos dé el porcentaje de la CLASE 2 basandose en sus datos
    # solo tenemos encuenta al que la IA a metido en el saco de la clase 2 el resto no los necesitamos.
    # multiplicamos por 100 para tener una nota sobre 100.
    nota = int(probabilidades[i][2]* 100)


    # Etiquetas
    etiqueta = ""
    if nota >= 90:
        if rol_detectado == "DEL" and goles > 40: etiqueta = " (¡Perfil atacante)"
        elif rol_detectado == "MCO" or rol_detectado == "ED": etiqueta = " (¡Perfil extremo!)"
        elif rol_detectado == "MCD" or rol_detectado == "CEN": etiqueta = " (¡Perfil Defensivo!)"
        elif rol_detectado == "MC" or rol_detectado == "MCO": etiqueta = " (¡Perfil Creador!)"

    Informe.append({
        'nombre': Id_candidatos[i] + etiqueta,
        'nota': nota,
        'rol': rol_detectado,
        'puntos': ListaPuntos[i]
    })

# La función sirve para que el programa sepa en qué campo fijarse para ordenar.
# Le decimos que el dato clave es la nota que le han puesto los expertos.
def por_nota(candidato):
    return candidato['nota']

# Ahora ordenamos la lista para que los mejores salgan arriba
Informe.sort(key=por_nota, reverse=True)

# Salida por terminal
print("\n Resultado de los 10 mejores segun la IA \n")
print("Nº | ROL | NOTA | G-A-R-P-KM-PJ | NOMBRE")
#G= goles - A = asistencias - R=Recuperaciones de balon - P = pases - KM =Kilometros recorridos - PJ =Partidos jugados
print("-------------------------------------------------------------------")

for i in range(10):
    f = Informe[i]  # Sacamos el diccionario del jugador actual (la i de la lista)
    p = f['puntos']  # Extraemos sus métricas para no escribir f['puntos'] tod0 el tiempo
    km_fijo = round(p[4], 1)  #Redondeo de los kilometros recorridos a un decimal

    print(i + 1, "  |", f['rol'], "|", f['nota'],
          "% |", int(p[0]), "-", int(p[1]), "-", int(p[2]), "-", int(p[3]), "-", km_fijo ,"-", int(p[5]),
          "|", f['nombre'])