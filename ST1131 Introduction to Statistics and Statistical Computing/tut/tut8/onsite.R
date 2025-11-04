setwd("C:/Users/USER/Documents/GitHub/nus/ST1131 Introduction to Statistics and Statistical Computing/tut/tut8")

df = read.csv("Anorexia.csv")

cont = df[df$therapy == "cont",c(3)]
cog = df[df$therapy == "cog",c(3)]
combined = cbind(cont,cog)
boxplot(combined,beside=T)

#looks effective as the change of weight is smaller

# Data collected has to be truly random, the group the people are in are categorical while the change in weight is qualitative

mu_cont = mean(cont)
mu_cog = mean(cog)
sd_cont = sd(cont)
sd_cog = sd(cog)
n_cont = length(cont)
n_cog = length(cog)

var.test(cont,cog)
sad = ((n_cont-1)*(sd_cont**2)+(n_cog-1)*(sd_cog**2))
sp = sqrt(sad/(n_cont+n_cog-2))
se = sp * (sqrt(1/33 + 1/32))
t = (mu_cont - mu_cog)/se
pt(t,65-2) * 2