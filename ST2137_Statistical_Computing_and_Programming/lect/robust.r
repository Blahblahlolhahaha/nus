setwd("c:\\Users\\sad\\Documents\\GitHub\\nus\\ST2137_Statistical_Computing_And_Programming\\lect")


chem = read.csv("data/mass_chem.csv")

hist(chem$chem, breaks=20, xlab="Amount of copper")
sort(chem$chem)
mean(chem$chem)
mean(sort(chem$chem)[1:22]) #two very different mean!

sample <- c(1:10)
sample

mean(sample)
mean(sample, trim = 0.1)

library(DescTools)
# will interpolate between two values if doesnt exist
vals <- quantile(chem$chem, probs = c(0.1, 0.9)) #determined by type
win_sample <- Winsorize(chem$chem, vals) #done differently in py
sort(win_sample)
sort(chem$chem)

mean(chem$chem, trim=0.1)

awareness <- c(77, 87, 88, 114, 151, 210, 219, 246, 253, 262, 296
               , 299, 306, 376, 428, 515, 666, 1310, 2611)

sd(awareness)
mad(awareness)
mad(awareness, constant = 1) # r will automatically multiply by 1.4826
IQR(awareness)