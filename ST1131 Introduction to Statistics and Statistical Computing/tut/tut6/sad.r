setwd("C:/Users/USER/Documents/GitHub/nus/ST1131 Introduction to Statistics and Statistical Computing/tut/tut6")

df = read.csv("104_plants.csv")
sad = c(1:100)
ci = c(1:100)
for(i in 1:100){
  row = df[i,]
  sus = sum(row == "susceptible")/104
  
  sad[i] = sus
}
Z = sad
sad
mean(sad)
