set.seed(2138)

z = matrix(rnorm(50*30), nrow = 50)

h <- 0.3
generated_y <- z*exp((h*z*z)/2)

x_bar <- apply(generated_y, 1, mean)
x_trim <- apply(generated_y, 1, mean, trim=0.1)

print(mean(x_bar))
print(mean(x_trim))

print(sd(x_bar))
print(sd(x_trim))

