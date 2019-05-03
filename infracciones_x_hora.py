# -*- coding: utf-8 -*-

import matplotlib.pyplot as plt
import matplotlib.animation as animation

# Create figure for plotting
fig = plt.figure()
ax = fig.add_subplot(1, 1, 1)
xs = []
ys = []

def load_infractions(file):
        with open(file, "r") as myfile:
            lines = myfile.readlines()
            
        #Diccionario cuya clave será el tipo de ruta y el valor una lista con infracciones. Cada infracción tendrá la velocidad a la que iba y la fecha.
        infractions={}
        for x in lines:
            line=x.split("&&&&")
            if line[0] in infractions:
                infractions[line[0]].append((line[1],line[2][:-1]))
            else:
                infractions[line[0]]=[(line[1],line[2][:-1])]
        return infractions

infractions=load_infractions('infractions_db')


# This function is called periodically from FuncAnimation
def animate(i, xs, ys):
    ax.clear()
    xs=[]
    ys=[]
    # Read temperature (Celsius) from TMP102
    #temp_c = round(random.randint(0, 5), 2)

    infractions=load_infractions('infractions_db')

    # Add x and y to lists
    #xs.append(dt.datetime.now().strftime('%H:%M:%S.%f'))
    #ys.append(temp_c)
    result={}
    
    for k, v in infractions.items():
        #v es una lista de pares. El segundo elemento es la fecha de la infracción.
        for x in v:
            
            #PONER LOS INDICES BIEN
            date=x[1].split('_')[4] #En la segunda posición se encuentran las horas.
            #date=date + ' del ' +x[1].split('_')[3]
            if date in result:
                result[date]=result[date] +1
            else:
                result[date]=1
                
    for k,v in result.items():
        xs.append(k)
        ys.append(v)
            
    #ys.append(len(v))


    # Limit x and y lists to 20 items
    #xs = xs[-20:]
    #ys = ys[-20:]

    # Draw x and y lists
    ax.clear()
    ax.plot(xs, ys)

    # Format plot
    plt.xticks(rotation=45, ha='right')
    plt.subplots_adjust(bottom=0.30)
    plt.title('Número de infracciones por día')
    plt.ylabel('Número de infracciones')    
    plt.xlabel('Día')

# Set up plot to call animate() function periodically
ani = animation.FuncAnimation(fig, animate, fargs=(xs, ys), interval=1000)
plt.show()





    
