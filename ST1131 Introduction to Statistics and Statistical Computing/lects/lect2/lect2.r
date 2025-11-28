#manually keying in by columns
response = c(6.1,5.9,5.8,5.4,6.3,6.2,5.8,6.3,7.1,8.2,7.3,6.9)
treatment = c("Control","Control","Control","Control","Pre-heated","Pre-heated","Pre-heated","Pre-heated","Pre-chilled","Pre-chilled","Pre-chilled","Pre-chilled")
df =data.frame(response,treatment)
df

matrix(c(response,treatment),12,2) #all numbers will be converted to string
df$response #get response column

setwd("C:/Users/USER/Documents/GitHub/nus/ST1131 Introduction to Statistics and Statistical Computing/lects/lect2") #set working directory

#header TRUE tells R that the first row is the header
#sep = "" ->separated by spaces
data1 <- read.csv("crab.txt",header=TRUE, sep="") 
data1[1:5,] #read first 5 rows

names(data1) #column names
names(data1)[4] = "sat" #change name of 4th column
names(data1) 


ex1 <- read.csv("ex_1.txt", header=FALSE, sep="")
ex1

names(ex1) <- c("Subject","Gender","CA1", "CA2", "HW") #<- assign column names to ex1
ex1

data1[172,"weight"] # get weight of 172nd row
data1[172,5] # get 5th column of 172nd row
data1[172,] #get entire 172nd row

#attach(data1) #allows direct access of the columns in data1
#weight #will search in data1 first
#weight[1] = 100
#data1$weight[1] == weight[1]

ex1[ex1$Gender == "M",] #filter by male, if attach, ex1$ can be omitted
ex1[ex1$Gender == "M", c("CA1","CA2")] #get results column!
ex1[ex1$CA1 >= 90,] #Get rows where CA1 >= 90
ex1[ex1$Gender == "M" & ex1$CA1 >= 85,] #males who scored 85 and above for CA1
ex1[ex1$Gender == "M" & (ex1$CA1 >= 85 | ex1$CA2 >=85),] #males who scored 85 and above for CA1/CA2

max(ex1$CA1) #max of CA1 
min(ex1$CA1) #min of CA1
sum(ex1$CA1) #sum all values in CA1
mean(ex1$CA1) #Avg CA1 score
range(ex1$CA1) #min and max of CA1
cor(ex1$CA1,ex1$CA2) #correlation of ca1 and ca2
colMeans(ex1[,c(1,3,4)]) #get means for columns 1,3,4. All values have to be numeric
which(ex1$CA2 == 84) #get row number where condition is true