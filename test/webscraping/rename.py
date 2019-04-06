
import os 

i = 1
  
for filename in os.listdir("potato/"): 
	dst = str(i) + ".jpg"
	src ='potato/'+ filename 
	dst ='potato/'+ dst 
	  
	# rename() function will 
	# rename all the files 
	os.rename(src, dst) 
	i += 1