
y_vals <- rep(c(1, 2, 3, 5), times=c(12, 14, 3, 1))

alpha <- 0.001
beta <- 1000
integrand_fn <- function(lambda, y, alpha, beta) {
  n <- length(y)
  log_value <- (sum(y) + alpha - 1) * log(lambda) - lambda * (n + 1 / beta) -
    n * log(1 - exp(-lambda))
  exp(log_value)
}
g <- Vectorize(integrand_fn, vectorize.args = c("lambda"))
K = integrate(g, 0, Inf, y = y_vals, alpha = alpha, beta = beta)
print(K)

lam_val <- seq(0, 3, by=0.001)
sad <- g(lam_val, y = y_vals, alpha = alpha, beta = beta)

plot(lam_val, sad, type = "l", lwd = 2, ylab = "Density", xlab = "λ", main = "Posterior density of λ")
