plot(1:10, 2*(1:10))

x <- 1 : 10
y <- 2 * x

f <- function() {
  print("hello world")
}

f()


#numeric vector:
numeric_vec <- c(1,2,3,4)

#string vec 
string_vec <- c("hello", "die")

#factors vec
factors_vec <- factor(c("male","male", "female"))

#logical vec
logical_vec <- c(TRUE, FALSE, TRUE)

as.character(numeric_vec)
as.numeric(string_vec) # will die
as.numeric(c("1","2"))
as.numeric(logical_vec) # T = 1, F = 0
as.numeric(factors_vec)
mean(logical_vec)


r1 = rep(2, 3) # first one is repeating element, 2nd is number of times to repeat
r2 = rep(c(1,2), 3) #vector is repeated
r3 = rep(c(1,2), c(4,5)) # repeat 1 for 4 times, repeat 2 5 times
r4 = rep(c(1,2), each=3)
?rep # pulls out help page


seq(from=2, to=10, by=2) #for(int i = 2; i <= 10; i += 2)
seq(from=0, to=12, length=4) #auto determine separation
x <- seq(2, 5, 0.8) 
x * 2
x * c(1,2) # reuse elements *1 *2 *1 *2 ...
x * c(1,2,3) # if not multiple, will throw warning

s1 <- 2:6
s1[1]


numeric_vec[seq(1,5,by=2)] # selects element 1 3 5
numeric_vec[-seq(1,5, by=2)] #removes element 1 3 5 

v <- c(1:6)
m1 <- matrix(v, nrow=2, ncol=3)

a <- c(1,2,3,4)
b <- c(5,6,7,8)

ab_row = rbind(a,b)
ab_row

cbind(m1, c(7,8))

m1[1, ] # returns row 1
m1[1, 3] #returns row 1 col 3
m1[1, c(1,3)]
