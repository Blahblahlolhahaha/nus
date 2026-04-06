setwd("C:\\Users\\sad\\Documents\\GitHub\\nus\\ST2137_Statistical_Computing_and_Programming\\tut\\tut10")

library("MASS")
cars <- Cars93
cars <- cars[cars$Cylinders != "rotary", ]
cars$Cylinders <- as.integer(Cars93$Cylinders)
boxplot(MPG.city ~ Cylinders, data=cars)



model <- lm(MPG.city ~ Cylinders, data=cars)
anova(model)
summary(model)

pop <- read.csv("sg_population.csv")
colnames(pop) <- c("year","pop")
lines(pop)
k <-  7 * 10 ** 6
pop$log <- log(k / pop$pop - 1) 
model3 = lm(log ~ year, data = pop)

summary(model3)
new_year <- data.frame(year=seq(1950,2050,1))
new_year$log <- predict(model3, newdata = new_year)
new_year$pop <- k / (exp(new_year$log)+1)
plot(new_year$year, new_year$pop, col = "coral")
lines(new_year$year, new_year$pop, col = "coral")
lines(pop$year, pop$pop, col = "violet")

crab <- read.csv("crab.txt", sep = " ")
crab$spine = as.factor(crab$spine)
crab_lm = lm(weight ~ width * spine, data = crab)
summary(crab_lm)
ss_res = sum(summary(crab_lm)$residuals ^ 2)
sigma2 = ss_res /summary(crab_lm)$Residuals$df

library(Hmisc)
#pak::pak("Hmisc")
 
#?areg
output <- areg(crab[,1:4], crab$weight, xtype=c("c", "c", "s", "s"), ytype="s")
plot(output)
