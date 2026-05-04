housing = read.csv("data/taiwan_dataset.csv")
u_stores <- sort(unique(housing$num_stores))
housing$num_stores = factor(housing$num_stores, levels = u_stores)

sad <- boxplot(housing$price)
housing = housing[housing$price != sort(sad$out, decreasing=TRUE)[1],]

housing_lm = lm(price ~ num_stores, data = housing)



summary_out <- anova(housing_lm)
est_coef <- coef(housing_lm)
est_coef[2:length(est_coef)] = est_coef[-1] + est_coef[1]

data <- aggregate(price ~ num_stores,
    data = housing,
    sd
)
sds <- data$price
ratio <- max(sds) / min(sds)
print(ratio)

resi <- residuals(housing_lm)
# hist(resi)
qqnorm(resi)
qqline(resi)

head(housing)

c1 <- c(rep(0.2, 5), rep(-1/6, 6))

get_num_of_values <- function(x) {
    NROW(housing[housing$num_stores == x,])
}

n_values <- sapply(u_stores, get_num_of_values)
c1 * est_coef
L <- sum(c1 * est_coef)
MSW <- summary_out$`Mean Sq`[2]
df <- summary_out$Df[2]
se1 <- sqrt(MSW * sum(c1^2 / n_values))
q1 <- qt(0.025, df, 0, lower.tail = FALSE)
lower_ci <- L - q1*se1
upper_ci <- L + q1*se1
cat("The 95% CI for the diff. between the two groups is (",
    format(lower_ci, digits = 2), ",",
    format(upper_ci, digits = 2), ").",
    sep = ""
)

generate_z1 <- function(x1, x2) {
    sqrt(-2 * log(x1)) * cos(2 * pi * x2)
}

generate_z2 <- function(x1, x2) {
    sqrt(-2 * log(x1)) * sin(2 * pi * x2)
}

two_n_function <- function(n) {
    res <- rep(0, 2 * n)
    for (i in 1:n) {
        u1 <- runif(1, 0, 1)
        u2 <- runif(1, 0, 1)
        res[i * 2 - 1] <- generate_z1(u1, u2)
        res[i * 2] <- generate_z2(u1, u2)
    }
    res
}

res = 0
for (i in 1:1000) {
    test <- two_n_function(30)
    shapi <- shapiro.test(test)
    if (shapi$p.value < 0.1) {
        res = res + 1
    }
}
print(res)

mean1 = 10000
var1 = 1000000
a = (mean1**2) / var1
scale = var1 / mean1
set.seed(2002)
get_profit <- function(demand, C) {
    ifelse(demand >= C, C, floor(demand) + (C - floor(demand)) * -0.25)
}

simulate_year <- function(C) {
    demand = rgamma(30, shape = a, scale = scale)
    sum(vapply(demand, get_profit, FUN.VALUE = numeric(1), C = C))
}

sad = seq(1:50000)

res = sapply(sad, simulate_year)

df = data.frame(C = sad, profit=res)

plot(df$C, df$profit)

print(df[df$profit == max(df$profit), "C"])



