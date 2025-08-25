#setwd("C:/Users/USER/Documents/GitHub/nus/ST1131 Introduction to Statistics and Statistical Computing/tut/tut1")
#df = read.csv("final_marks.csv",header=TRUE, sep=",")
patient = 1:9
readings = c(96,119,119,108,126,128,110,105,94)
df <- data.frame(patient,readings)
df
sorted = sort(df$readings)
sorted
size = length(sorted)
size
median = sorted[5]
median
test = median(sorted)
test
test == median
mean(df$readings)
var(df$readings)
sd(df$readings)

new_df = df
new_df$readings = new_df$readings - 10
mean(new_df$readings)
var(new_df$readings)

boxplot(new_df$readings, main="Blood Pressure Readings",ylab="Blood Pressure (mm Hg)")
unique_bp = data.frame(readings=unique(new_df$readings),prob=numeric(8))
count <- table(new_df$readings)
count
unique_bp$prob = count[as.character(unique_bp$readings)]/9


unique_bp

hist(unique_bp$readings,l=4, prob=TRUE)

