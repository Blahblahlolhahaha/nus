qt(0.95, 11) # Under H0, T ~ T(11) -> t dist with 11 degrees of freedom

# xi -> before measurement
# yi -> after measurement
# T2 = (Dbar - O) / (s/sqrt(n))
# P(T2 < 1.796) = 0.95
# P(Dbar - 1.796 * (s/(sqrt(n))) < (mub - mua)) = 0.95 <- one sided Confidence Interval

setwd("c:\\Users\\USER\\Documents\\GitHub\\nus\\ST2137_Statistical_Computing_And_Programming\\lect")
heart <- read.csv("data\\health_promo_hr.csv")

before <- heart$baseline
after <- heart$after5

t.test(before, after, paired = TRUE)
t.test(before - after)

t.test(before, after, paired = TRUE, alternative = "greater")
t.test(before, after, paired = TRUE, alternative = "greater", mu = -10)

# 2 sample test  
abalone <- read.csv("data\\abalone_sub.csv")
x <- abalone$viscera[abalone$gender == "M"]
y <- abalone$viscera[abalone$gender == "F"]

wilcox.test(x, y)

wilcox.test(before, after, paired = TRUE, exact = FALSE) #<- cannot compute exact p-value with ties