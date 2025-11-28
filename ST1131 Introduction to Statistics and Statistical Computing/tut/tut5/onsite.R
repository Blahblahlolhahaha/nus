sad = rnorm(45,mean=2.8,sd=2.1)
mean(sad)
sd(sad)
sad <- replicate(500000, rnorm(100,mean=2.8,sd=2.1))
#N = 1000
#sad = matrix(rnorm(45 * N, mean=2.8,sd=2.1),45,N) # alternative way
x.bar <- colMeans(sad)
hist(x.bar, col="lightblue", xlab="Mean call time",main="Sampling call duration")
mean(x.bar)
sd(x.bar)
proportion = length(x.bar[x.bar > 3.5])
proportion/1000