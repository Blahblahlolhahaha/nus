setwd("C:/Users/USER/Documents/GitHub/nus/ST1131 Introduction to Statistics and Statistical Computing/lects/lect3") #set working directory

data <- read.csv("midterm_marks.csv",header=TRUE, sep=",")
marks <- data[,2]
marks

#### SUMARIES
summary(marks)
length(marks)

min(marks)
mean(marks)
median(marks)
max(marks)

quantile(marks,0.3)
quantile(marks,0.25) #lower quartile
quantile(marks,0.75) #upper quartile
IQR(marks) #Interquartile range (upper - lower)
range(marks)
var(marks) #variance
sd(marks) #standard deviation

#### HISTOGRAM
hist(marks)
hist(marks, prob=TRUE)
hist(marks, prob=TRUE,col="lightblue", xlab="Midterm Marks", ylab="Density", xlim=range(marks),breaks=10)


###BOXPLOT
boxplot(marks)

boxplot.stats(marks) # display stats for marks such as confidence and outliers

boxplot(marks, main="Boxplot of Midterm Marks", ylab="Midterm marks",col="lightblue")
abline(h=median(marks), col="black")

marks = c(marks,45)
boxplot(marks, main="Boxplot of Midterm Marks", ylab="Midterm marks",col="lightblue")
marks = c(marks,-10)
boxplot(marks, main="Boxplot of Midterm Marks", ylab="Midterm marks",col="lightblue")


cancer <- read.csv("lung_cancer.csv", header = TRUE)
cancer

table(cancer$Gender)
prop.table(table(cancer$Gender)) #shows proportion of female:male

cancer$Gender <- ifelse(cancer$Gender == "0", "Female", "Male") # Use if else to rename the columns
cancer

cancer$Gender <- as.factor(cancer$Gender) # Tells r that the variable is catagorical
levels(cancer$Gender) #shows the diff categories of the variable
levels(cancer$Gender) <- c("Female","Male") #rename the category
cancer

### barplot
barplot(table(cancer$Gender), col=c("pink","lightblue"),xlab="Gender",ylab="Number of people", main="Bar plot of Gender")

pie(table(cancer$Gender), col=c("pink","lightblue"), main="Pie chart of Gender")
