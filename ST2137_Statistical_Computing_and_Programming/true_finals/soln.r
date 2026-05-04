setwd("c:\\Users\\sad\\Documents\\GitHub\\nus\\ST2137_Statistical_Computing_And_Programming\\true_finals\\data")

library("MASS")

cars <- Cars93
model_1 <- lm(MPG.city ~ Width, data = cars)
model_1_summary <- summary(model_1)
print(model_1_summary$adj.r.squared)

plot(cars$MPG.city, cars$Width)
abline(model_1_summary, col = "deepskyblue", lty = 2)

mean_diff <- function(x) {
    (
        x - mean(cars$Width)
    )
}

mean_diff_sq <- function(x) {
    (
        (x - mean(cars$Width))**2
    )
}

cars["W1"] <- sapply(cars$Width, FUN = mean_diff)
cars["W2"] <- sapply(cars$Width, FUN = mean_diff_sq)

model_2 <- lm(MPG.city ~ W1 + W2, data = cars)
model_2_summary <- summary(model_2)
print(model_2_summary$adj.r.squared)

r_s <- abs(rstandard(model_2))
r_s_sorted <- sort(r_s, decreasing = TRUE)
r_s_sorted[c(1, 2)]
indexes <- c(42, 39)
cars["W3"] <- rep(0, NROW(cars))
cars["W3"][indexes, ] <- 1

model_3 <- lm(MPG.city ~ W1 + W2 + W3, data = cars)
model_3_summary <- summary(model_3)
print(model_3_summary$adj.r.squared)
plot(cars$MPG.city, cars$Width)
abline(model_3_summary, col = "deepskyblue", lty = 2)

generate_one_permutation <- function(groups, measurements) {
    new_groups <- sample(groups)
    df <- data.frame(groups = new_groups, measurements = measurements)
    lm1 <- lm(measurements ~ groups, data = df)
    anova_model <- anova(lm1)
    anova_model$"F value"[1]
}


heifers <- read.csv("antibio.csv")

f_values <- replicate(10000, generate_one_permutation(groups = heifers$type, measurements = heifers$org))

hist(f_values)

lm2 <- lm(org ~ type, data = heifers)
anova_model2 <- anova(lm2)
p_val <- 2 * mean(f_values > anova_model2$"F value"[1])
