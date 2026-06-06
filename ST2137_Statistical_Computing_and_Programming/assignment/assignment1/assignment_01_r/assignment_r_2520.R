
# Question 1
# uncomment and define your function here:
f <- function(lambda, y) {
  return(mean(y) - lambda / (1 - exp(-lambda)))
}


y_vals <- rep(c(1, 2, 3, 5), times=c(12, 14, 3, 1))
print(f(2.3, y_vals))

# Question 2
# Use uniroot with f() here:

mle_lambda <- uniroot(f, lower = 1, upper = 10, y = y_vals)
mle_lambda <- mle_lambda$root
print(mle_lambda)
# And so on..

alpha <- 0.001
beta <- 1000
integrand_fn <- function(lambda, y, alpha, beta) {
    n <- length(y)
    log_value <- (sum(y) + alpha - 1) * log(lambda) - lambda * (n + 1 / beta) -
        n * log(1 - exp(-lambda))
    exp(log_value)
}
g <- Vectorize(integrand_fn, vectorize.args = c("lambda"))
K <- integrate(g, 0, Inf, y=y_vals, alpha=alpha, beta=beta, abs.tol = 1.0e-16)
K <- K$value

lam_val <- seq(0, 3, by=0.001)
sad <- g(lam_val, y = y_vals, alpha = alpha, beta = beta) / K

plot(lam_val, sad, type = "l", lwd = 2, ylab = "Density", xlab = "λ", main = "Posterior density of λ")

df <- data.frame("x" = lam_val, "y" = sad)
df <- na.omit(df)
posterior_mode_lambda <- df[df["y"] == max(df["y"]), 1]
