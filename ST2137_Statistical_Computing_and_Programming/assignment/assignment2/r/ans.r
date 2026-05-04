setwd("c:\\Users\\sad\\Documents\\GitHub\\nus\\ST2137_Statistical_Computing_And_Programming\\assignment\\assignment2\\r")

fev <- read.table("data/FEV.DAT", header = TRUE)

fev$Gender  <- factor(x=fev$Gender, levels=sort(unique(fev$Gender)), labels = c( "female", "male"))
fev$Smoke  <- factor(x=fev$Smoke, levels=sort(unique(fev$Smoke)), labels = c( "No", "Yes"))


model <- lm(FEV ~ Age + Ht + Gender + Smoke, data = fev)
sad <- summary(model)
r_s <- sort(abs(rstandard(model)), decreasing = TRUE)
r_s[1:3]
fev[c(624,648,452),]
anova(model)

model2 <- lm(FEV ~ Ht + Gender + Ht:Gender, data = fev)
summary(model2)

model2 <- lm(FEV ~ Ht * Gender, data = fev)
summary(model2)

rpois()

model2 <- lm(FEV ~ Ht:C(Gender, base=1), data = fev)
summary(model2)

sigma2 <- sigma(model) ** 2
adj_r2 <- sad$adj.r.squared
fstat <- sad$fstatistic[1]

library(lattice)
xyplot(FEV ~ Ht| Gender * Smoke, data = fev)

female_no = fev[(fev$Gender == "female" & fev$Smoke == "No"),]

new_df_6 <- data.frame(Ht = 45:80, Smoke = "No", Gender = "female", Age = 6)
fitted_6 <- predict(model, new_df_6)

new_df_10 <- data.frame(Ht = 45:80, Smoke = "No", Gender = "female", Age = 10)
fitted_10 <- predict(model, new_df_10)

new_df_14 <- data.frame(Ht = 45:80, Smoke = "No", Gender = "female", Age = 14)
fitted_14 <- predict(model, new_df_14)

plot(female_no$Ht, female_no$FEV, col = "darkgreen", xlab = "Height", ylab = "FEV")
#plot impt variables
lines(x = 45:80, y = fitted_6, col = "deepskyblue")
lines(x = 45:80, y = fitted_10, col = "pink")
lines(x = 45:80, y = fitted_14, col = "violet")

legend(45, 3.5, legend=c("Age = 6", "Age = 10", "Age = 14"),
       col=c("deepskyblue", "pink", "violet"), lty=1:2, cex=0.8)

robust_skew <- function(data) {
quantiles <- quantile(data, probs = c(0.25, 0.5, 0.75), names = FALSE)
(quantiles[3] + quantiles[1] - 2 * quantiles[2]) / (quantiles[3] - quantiles[1])
}

robust_kurt <- function(data) {
quantiles <- quantile(data, probs = seq(0.125, 1, 0.125), names=FALSE)
deno <- (quantiles[6] - quantiles[2])
numer <- (quantiles[7] - quantiles[5] + quantiles[3] - quantiles[1])
numer / deno - 1.23
}

robust4 = function(data) {
    c(
        trimmed = mean(data, trim = 0.1),
        MAD = mad(data),
        Robust_Skew = robust_skew(data),
        Robust_Kurt = robust_kurt(data)
    )
}
fev$rs = rstandard(model)
sad <- aggregate(rs ~ Gender + Smoke, data=fev, FUN=robust4)
