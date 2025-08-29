setwd("C:/Users/USER/Documents/GitHub/nus/ST1131 Introduction to Statistics and Statistical Computing/tut/tut1")
df = read.csv("final_marks.csv",header=TRUE, sep=",")

length(df) #number of columns
head(df) #show first few rows
tail(df) #show last few roms
dim(df) #dimension -> rows/columns
names(df)[2] = "mark"

mean(df$mark)
median(df$mark)
var(df$mark)
yes = range(df$mark)
yes[2] - yes[1] #get range

quartiles = quantile(df$mark, prob=seq(0,1,0.25), names=FALSE)
IQR = quartiles[4] - quartiles[2] #IQR
q1_outlier = quartiles[2] - 1.5 * IQR
q3_outlier = quartiles[3] + 1.5 * IQR
boxplot(df$mark) #yes have outlier
df$mark[df$mark < q1_outlier | df$mark > q3_outlier] #outlier

stats = boxplot.stats(df$mark)
stats$out #faster way to show outlier
hist(df$mark,xlab = "Scores") #default 9 bins?
hist(df$mark,breaks=20) #set number of bins to 5




