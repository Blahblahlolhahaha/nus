setwd("c:\\Users\\sad\\Documents\\GitHub\\nus\\ST2137_Statistical_Computing_And_Programming\\lect")

sad <- read.csv("data\\concrete+slump+test\\slump_test.data")

lm_flow_water <- lm(FLOW.cm. ~ Water + Slag, data=sad)
summary(lm_flow_water)

plot(y=sad$FLOW.cm., x=sad$Slag)

summary(sad$Water)

new_df <- data.frame(Slag = 0:200, Water = 180)
fitted_1q <- predict(lm_flow_water, new_df)
head(fitted_1q)

new_df2 <- data.frame(Slag = 0:200, Water = 209.5)
fitted_3q <- predict(lm_flow_water, new_df2)
head(fitted_1q)

#plot impt variables
lines(x = 0:200, y = fitted_1q, col="blue")
lines(x = 0:200, y = fitted_3q, col="blue")

bike2 <- read.csv("data\\bike2.csv")
bike2_sub <- bike2[bike2$workingday == "no",]
lm_reg_casual1 <- lm(registered ~ casual, data = bike2_sub)
lm_reg_casual2 <- lm(registered ~ casual + workingday, data = bike2)
summary(lm_reg_casual1)
summary(lm_reg_casual2)

plot(x=bike2$casual, 
y=bike2$registered, 
col=ifelse(bike2$workingday == "yes", "salmon", "deepskyblue4"),
main="Comparing fitted models",cex=0.8,xlab="Casual", ylab="Registered"
)

abline(lm_reg_casual2, col="deepskyblue", lty=2)
est_coef <- coef(lm_reg_casual2)
abline(est_coef[1], est_coef[2], col="deepskyblue", lty=2)
abline(est_coef[1] + est_coef[3], est_coef[2], col="salmon", lty=2)

lm_reg_casual3 <- lm(registered ~ casual * workingday, data = bike2)
summary(lm_reg_casual3)
abline(lm_reg_casual3, col="red", lty=2)
est_coef2 <- coef(lm_reg_casual3)
abline(est_coef2[1], est_coef2[2], col="purple", lty=2)
abline(est_coef2[1] + est_coef2[3], est_coef2[2] + est_coef2[4], col="limegreen", lty=2)

r_s <- rstandard(lm_flow_water)
hist(r_s)
qqnorm(r_s)
qqline(r_s)

shapiro.test(r_s)
ks.test(r_s, "pnorm")

plot(lm_flow_water)

