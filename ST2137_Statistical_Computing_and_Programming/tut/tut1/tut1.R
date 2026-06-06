x = c(50:55)
y = c(4,4.7,4.7,5.9,6.6,7.3)

df1 = data.frame(x = x, y = y)
df1

phones = MASS::phones
phones = data.frame(x = phones$year, y = phones$calls) 

setwd("C:\\Users\\USER\\Documents\\GitHub\\nus\\ST2137_Statistical_Computing_and_Programming\\tut\\tut1")
write.csv(phones, "data/phones-2420.csv", row.names = FALSE) # default will have row names


nrow(phones)
phones$y
NROW(phones[phones$y >= 100 & phones$y <= 200,])
sorted = sort(phones$y)
sorted[1:3]

sorted[-(1:(length(sorted) - 3))]
tail(sorted, n=3)

#find largest
largest = sorted[-(1:(length(sorted) - 1))][1]
phones$x[phones$y == max(phones$y)]
phones$x[which.max(phones$y)]

ones = rep(1,24)
X = matrix(cbind(ones,phones$x), ncol = 2)
Y = matrix(phones$y)

solve(t(X) %*% X) %*% t(X) %*% Y

X %*% solve(t(X) %*% X) %*% t(X) %*% Y

lm_output <- lm(y ~ x, data=phones)

sad = combn(1:NROW(phones),2)
gg = c()
rr = data.frame(t(sad))

for (i in 1:NROW(rr)) {
  grad = (phones$y[rr$X2[i]] - phones$y[rr$X1[i]]) / (phones$x[rr$X2[i]] - phones$x[rr$X1[i]])
  gg = c(gg, grad)
}
median(gg)
