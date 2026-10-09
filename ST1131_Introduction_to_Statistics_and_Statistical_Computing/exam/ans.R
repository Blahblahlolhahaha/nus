setwd("C:/Users/USER/Documents/GitHub/nus/ST1131_Introduction_to_Statistics_and_Statistical_Computing/exam")

penguins = read.csv("penguins_practical.csv")
penguins = penguins[which(!is.na(penguins$sex)),]

len = nrow(penguins)

print(nrow(penguins[penguins$species == "Adelie",]))
print(nrow(penguins[penguins$species == "Chinstrap",]))
print(nrow(penguins[penguins$species == "Gentoo",]))
print(nrow(penguins[penguins$species == "NA",]))

print(nrow(penguins[penguins$sex == "male",]))
print(nrow(penguins[penguins$sex == "female",]))
print(nrow(penguins[penguins$sex == "NA",]))

adelie = penguins[penguins$species == "Adelie",]
torg =  penguins[penguins$island == "Torgersen",]
print(nrow(torg[torg$species == "Adelie",]) / nrow(torg))

adelie_torg = penguins[penguins$species == "Adelie" & penguins$island == "Torgersen",]
torg =  penguins[penguins$island == "Torgersen",]
print(nrow(torg[torg$species == "Adelie",]) / nrow(torg))

mean(adelie$body_mass_g)

print(boxplot.stats(adelie$body_mass_g))

 boxplot(adelie$body_mass_g)



gentoo = penguins[penguins$species == "Gentoo",]

mean(gentoo$body_mass_g)
# boxplot(gentoo$body_mass_g)
print(boxplot.stats(gentoo$body_mass_g))

print(mean(gentoo$body_mass_g) - mean(adelie$body_mass_g))

print(shapiro.test(gentoo$body_mass_g))

# hist(adelie$body_mass_g)

print(var.test(adelie$body_mass_g, gentoo$body_mass_g))

print(t.test(gentoo$body_mass_g, adelie$body_mass_g, var.equal=TRUE, alternative="greater"))

chinstrap = penguins[penguins$species == "Chinstrap",]
chin_m = chinstrap[chinstrap$sex == "male",]
chin_f = chinstrap[chinstrap$sex == "female",]

p = nrow(chin_f) / nrow(chinstrap)

se = p * (1-p)/sqrt(nrow(chinstrap))

t = (p - 0.5)/se
print(t)
print(pnorm(t,lower.tail = FALSE))
# z = print(pnorm(0.975) * se)

# print(pnorm(z))

print(cor(penguins$bill_length_mm,penguins$body_mass_g))
print(cor(penguins$bill_depth_mm,penguins$body_mass_g))
print(cor(penguins$flipper_length_mm,penguins$body_mass_g))

m1 = lm(body_mass_g ~ flipper_length_mm + bill_length_mm + bill_depth_mm, data=penguins)

newdata = data.frame(flipper_length_mm=c(200),bill_length_mm=c(50),bill_depth_mm=c(20))

print(predict(m1, newdata=newdata))

print(summary(m1))

penguins$sex = as.factor(penguins$sex)
penguins$island = as.factor(penguins$island)
penguins$species = as.factor(penguins$species)

m2 = lm(body_mass_g ~ flipper_length_mm + bill_length_mm + bill_depth_mm + sex + island + species, data=penguins)
print(summary(m2))

newdata = data.frame(flipper_length_mm=c(200),bill_length_mm=c(50),bill_depth_mm=c(20),sex=c("female"),island=c("Dream"),species=c("Adelie"))
newdata$sex = as.factor(newdata$sex)
newdata$island = as.factor(newdata$island)
newdata$species = as.factor(newdata$species)
print(predict(m2, newdata=newdata))

m3 = lm(body_mass_g ~ flipper_length_mm + flipper_length_mm ^ 2, data = penguins)
m4 = lm(body_mass_g ~ flipper_length_mm, data = penguins)
print(summary(m3))
print(summary(m4))
newdata = data.frame(flipper_length_mm=c(200))
print(predict(m3, newdata=newdata))

