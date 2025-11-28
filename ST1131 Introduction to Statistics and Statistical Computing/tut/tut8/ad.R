# p < a, h0 rejected in favour of h1

# reject but true -> type I
# if h0 is true, will contain 100 95% of the time

setwd("C:/Users/USER/Documents/GitHub/nus/ST1131 Introduction to Statistics and Statistical Computing/tut/tut8")

df = read.csv("internet_hours.csv")


males = df[df$gender == "male",]
females = df[df$gender != "male",]

#response -> number of hours, quatitative
#explanatory -> gender, categorical
nmale = nrow(males)
nfemale = nrow(females)
sd_male = sd(males$hours)
sd_female = sd(females$hours)
mean_male = mean(males$hours)
mean_female = mean(females$hours)

var.test(males$hours,females$hours)
#unequal variance

se = sqrt((sd_male**2/nmale) + sd_female**2/nfemale)

T = (mean_male-mean_female)/se

df = (nmale + nfemale) -2

pt(T,df,lower.tail = FALSE) * 2

t.test(males$hours,females$hours,var.equal =FALSE,conf.level=0.99)

#p value > 0.01
# 