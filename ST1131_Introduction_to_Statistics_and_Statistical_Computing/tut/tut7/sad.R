#qn1
# Since most students should finish education in 18 years
#X - 3s should contain 18
#hence s = 18/(2*3) = 3

n = (1.96*3) ** 2
n

#qn2

# the urine samples are random
# sample size is large enuf
# the samples from the dog follows a normal distribution
# variable is categorical and each trial are independent from each other

# The hypothesis of the test is the dogs ability to detect the
# correct urine specimen is better than randomly guessing. Here the
# probability of the random guessing is 1/7. Hence H1 > 1/7

# here, np = 54 * 1/7 = 7.7ish

p0 = 1/7
phat = 22/54
phat-p0
sd = sqrt(p0*(1-p0))/sqrt(54)
Z = (phat - p0)/sd
Z

p = pnorm(Z,lower.tail=FALSE)
p

#p is very small, hence there is a strong evidence that using dogs 
#is better than random guessing

setwd("C:/Users/USER/Documents/GitHub/nus/ST1131 Introduction to Statistics and Statistical Computing/tut/tut7")

df = read.csv("TVhours_long.csv")

n = nrow(df)
xhat = mean(df$hours)
sd = sd(df$hours)

hist(df$hours, col="lightblue", xlab="hours", ylab="No. of people", main="fat fucks")

#no def not normally distrubuted, right skewed

mu = xhat

#1 The data collected by GSS is completely random,
#  The sample size 1298 is large enough even though the data is right skewed
#  The data is quantitative

#2 The hypothesis is that the average hours of tv people watched is not 4

T = (mu - 4)/(sd/sqrt(n))
T
pt(T,n-1) * 2

#reject H0

Z = qt(0.975,1297)*sd/sqrt(n)

CI = c(mu - Z, mu + Z)

t.test(df$hours,mu=4)