# Queueing — Answer Key

[Questions](../02-Queueing.md) · [Practice index](../README.md)

## MCQs

| Question | Answer | Explanation |
|---|---|---|
| Q1 | **C** | $P(A)=P(B)=1/6$. Only outcome $(4,3)$ satisfies both, so the intersection probability is $1/36$. |
| Q2 | **B** | $P(\text{flag})=0.95(0.02)+0.05(0.98)=0.068$. Bayes gives $0.019/0.068=19/68$. |
| Q3 | **D** | Merged rate is 10/hour, so mean wait is 0.1 hour = 6 minutes. A wins the exponential race with probability $6/10$. |
| Q4 | **A** | $P(X>7\mid X>3)=0.75^7/0.75^3=0.75^4=0.31640625$. |
| Q5 | **B** | $\rho=0.8$. An empty waiting queue means $L=0$ or $1$, hence $(1-\rho)(1+\rho)=1-\rho^2=0.36$. |
| Q6 | **C** | Stability requires $\lambda_i<\mu_i$ at every node. Effective rates include repeated visits. |

## Q7

- (a) **0.2 s**.
- (b) **0.4 s**.
- (c) **1 s**.
- (d) **0.5 s**.
- (e) **Memorylessness** (of the exponential distribution).

With $l$ other jobs in the system, the conditional mean is $(l+1)/\mu$. In (c), $l=4$: three waiting and one in service. The unconditional mean is $1/(5-3)=0.5$ s. Residual service has mean $1/\mu$ even if the ongoing service has already lasted some time.

## Q8

- (a) **80 packets/s**.
- (b) **75 packets/s**.
- (c) **$200/3\approx66.6667$ packets/s**.
- (d) **$200/3\approx66.6667$ packets/s**.
- (e) **30 ms**.
- (f) **False**.

The separate bounds are:

$$
\lambda\le0.8(100)=80,\qquad
\lambda\le100-1/0.04=75,
$$

$$
\frac{\lambda}{100(100-\lambda)}\le0.02
\quad\Longrightarrow\quad3\lambda\le200.
$$

Take the smallest bound. At $\lambda=200/3$, utilization is $2/3$, $\bar W=0.03$ s, and $\bar D=0.03-0.01=0.02$ s. A bound on the mean is not a bound on every packet's delay.

## Q9

- (a) **0.25 s**.
- (b) **0.15 s**.
- (c) **$(6,4)$ packets/s**.
- (d) **0.5 s**.
- (e) **True**.

Pooled arrival rate is 6, so $\bar W=1/(10-6)=0.25$ and $\bar D=0.25-1/10=0.15$. Equal separate mean times require $\mu_1-4=\mu_2-2$, together with $\mu_1+\mu_2=10$.

For (e), positive spare capacities $s_1,s_2$ give $1/(s_1+s_2)<\min(1/s_1,1/s_2)$. This claim concerns **sojourn time**; an analogous improvement over every individual queueing delay is not guaranteed.

## Q10

- (a) **$(2,4,2)$ jobs/s**.
- (b) **$11/3\approx3.66667$ jobs**.
- (c) **$11/6\approx1.83333$ s**.
- (d) **2**.
- (e) **$1/45\approx0.0222222$**.

Traffic equations: $\lambda_1=2$, $\lambda_2=2+\lambda_3$, $\lambda_3=\lambda_2/2$. Utilizations are $(1/2,2/3,2/5)$, all below 1. Node mean populations are $(1,2,2/3)$.

Network Little's law divides by **external rate 2**, so $\bar W=(11/3)/2$. Equivalently, visit counts $(1,2,1)$ give $1(1/2)+2(1/2)+1(1/3)=11/6$ s.

Product form gives:

$$
P(L_1=1,L_2=2,L_3=0)
=\left(\frac12\frac12\right)
\left(\frac13\left(\frac23\right)^2\right)
\left(\frac35\right)=\frac1{45}.
$$

## Q11

- (a) **4 jobs/s**.
- (b) **$1/4=0.25$**.
- (c) **$9/16=0.5625$**.
- (d) **$1/3\approx0.333333$**.
- (e) **$7/12\approx0.583333$**.

$\lambda_1=2$ and $\lambda_2=2+\lambda_2/2=4$, giving $\rho_1=\rho_2=1/2$.

Both systems empty: $(1-\rho_1)(1-\rho_2)$. Both waiting queues empty: $(1-\rho_1^2)(1-\rho_2^2)$. Equal system populations:

$$
\sum_{n=0}^{\infty}(1-\rho_1)(1-\rho_2)(\rho_1\rho_2)^n
=\frac{1/4}{1-1/4}=\frac13.
$$

Equal waiting populations need a separate zero case:

$$
P(Q_1=Q_2)=\frac9{16}+\frac14\frac{(1/4)^2}{1-1/4}
=\frac9{16}+\frac1{48}=\frac7{12}.
$$

For positive waiting count $q$, each system has $q+1$ jobs, explaining why the series starts at exponent 2.

## Q12

- (a) **$\rho/(1-\rho)^2$**.
- (b) **$\rho^2/(1-\rho)$**.
- (c) **$e^{-\alpha t}$** (equivalently $P(T>t)$).
- (d) **Poisson**; **$\lambda$**.
- (e) **$k$**.
- (f) **Deterministic** (constant).

For (a), differentiate $\sum_{n\ge0}\rho^n=1/(1-\rho)$ and multiply by $\rho$. For (b), $\bar Q=\rho/(1-\rho)-\rho$. For (c), the exponential survival ratio cancels $s$. For (e), $1/(\mu/k-\lambda/k)=k/(\mu-\lambda)$; unchanged utilization does not imply unchanged delay.
