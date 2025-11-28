sad = rbinom(500,50,0.7)
hist(sad, breaks=8,col="lightblue")

pbinom(25,500,0.04,lower.tail=FALSE)

pnorm(0.05,0.04,sqrt((0.96*0.04)/500), lower.tail=FALSE)


pnorm(0.6,0.7,0.06481)
