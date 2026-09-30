# Chapter 2: Queueing Models

[Reference index, notation and formula lookup](README.md)

## Probability toolkit

Sources: Queueing lecture; Tutorials 1–3; Assignment Q1.

### Independence, conditioning and total probability

$$
P(A\mid B)=\frac{P(A\cap B)}{P(B)},\qquad
P(A\cap B)=P(A\mid B)P(B).
$$

Independence means $P(A\cap B)=P(A)P(B)$. Mutually exclusive events cannot occur together; positive-probability mutually exclusive events are not independent.

For a disjoint, exhaustive partition $B_1,B_2,\ldots$:

$$
P(A)=\sum_iP(A\mid B_i)P(B_i),\qquad
P(B_k\mid A)=\frac{P(A\mid B_k)P(B_k)}{\sum_iP(A\mid B_i)P(B_i)}.
$$

$$
E[X]=\sum_xxP(X=x)\quad\text{or}\quad\int_{-\infty}^{\infty}xf_X(x)\,dx,
\qquad E[X]=\sum_iE[X\mid B_i]P(B_i).
$$

For continuous conditioning, replace the sum by an integral against the conditioning variable's density. $F_X(x)=P(X\le x)$ and, when a density exists, $f_X(x)=F'_X(x)$.

Tutorial 1 reminders:

- For two fair dice, “sum is 7” and “second die is 4” **are independent**: $1/36=(1/6)(1/6)$. Verify mathematically rather than relying on intuition.
- Rain prediction: $P(\text{rain}\mid\text{forecast rain})=\frac{0.9(10/365)}{0.9(10/365)+0.1(355/365)}\approx0.20225$. A conditional probability is not generally equal to its reverse.

### Binomial: simultaneous active users

For $N$ independent users each active with probability $p$, $X\sim\mathrm{Binomial}(N,p)$:

$$
P(X=k)=\binom Nk p^k(1-p)^{N-k},\quad
P(X>m)=\sum_{k=m+1}^N\binom Nk p^k(1-p)^{N-k}
=1-\sum_{k=0}^m\binom Nk p^k(1-p)^{N-k}.
$$

Useful derived moments: $E[X]=Np$, $\operatorname{Var}(X)=Np(1-p)$.

If each active user needs rate $r$ on capacity $C$, at most $m=\lfloor C/r\rfloor$ fit simultaneously. Circuit switching supports $m$ reserved users; packet switching can admit more with an overload risk. For $N=35,p=0.1,m=10$, $P(X>10)\approx0.0004243$ (the lecture's “less than .0004” is a numerical approximation error).

### Exponential: time until next event

For $T\sim\mathrm{Exp}(\lambda)$, $t\ge0$:

$$
F_T(t)=1-e^{-\lambda t},\quad P(T>t)=e^{-\lambda t},\quad
f_T(t)=\lambda e^{-\lambda t},\quad E[T]=\frac1\lambda.
$$

Useful derived forms:

$$
P(a<T\le b)=e^{-\lambda a}-e^{-\lambda b},\quad
t_q=\frac{-\ln(1-q)}\lambda,\quad
\lambda=\frac{-\ln P(T>t)}t,\quad
\operatorname{Var}(T)=\frac1{\lambda^2}.
$$

**Memorylessness:** after already waiting $s$, the residual waiting-time distribution is unchanged:

$$
P(T>s+t\mid T>s)=\frac{e^{-\lambda(s+t)}}{e^{-\lambda s}}
=e^{-\lambda t}=P(T>t),\qquad E[T-s\mid T>s]=\frac1\lambda.
$$

This is stronger than merely saying the process depends on its current state. In Tutorial 2, knowing how long ago the last bus left does not shorten the expected residual wait.

### Poisson processes, merging, splitting and races

A Poisson process has i.i.d. exponential inter-arrival times. Counts over disjoint time intervals are independent. Useful count form (companion to the lecture's inter-arrival definition):

$$
P(N(t)=k)=e^{-\lambda t}\frac{(\lambda t)^k}{k!},\qquad E[N(t)]=\lambda t.
$$

- **Merge independent streams:** $\lambda_{\rm total}=\sum_i\lambda_i$.
- **Independently route each arrival to branch $i$ with probability $p_i$:** branch rate is $p_i\lambda$.
- For independent $T_i\sim\mathrm{Exp}(\lambda_i)$:

$$
P(\min_iT_i>t)=\prod_i e^{-\lambda_i t}=e^{-(\sum_i\lambda_i)t},\qquad
E[\min_iT_i]=\frac1{\sum_i\lambda_i},\qquad
P(T_i=\min_jT_j)=\frac{\lambda_i}{\sum_j\lambda_j}.
$$

Tutorial 3 race derivation:

$$
P(T_1<T_2)=\int_0^\infty P(T_2>t)f_{T_1}(t)\,dt
=\int_0^\infty\lambda_1e^{-(\lambda_1+\lambda_2)t}\,dt
=\frac{\lambda_1}{\lambda_1+\lambda_2}.
$$

### Geometric: watch the counting convention

Let $p$ be success probability and $q=1-p$.

| Convention | Support | PMF | Tail (integer $n\ge0$) | Mean |
|---|---|---|---|---|
| $X$: trials **including** first success | $1,2,\ldots$ | $P(X=k)=q^{k-1}p$ | $P(X>n)=q^n$ | $1/p$ |
| $Y=X-1$: failures before first success | $0,1,\ldots$ | $P(Y=k)=q^kp$ | $P(Y\ge n)=q^n$ | $q/p$ |

Both have variance $q/p^2$. For integer $n\ge0$, $P(X\le n)=1-q^n$ and $P(Y\le n)=1-q^{n+1}$. For real arguments use the floor; CDFs are zero below their support.

**Assignment Q1 uses $X$ starting at 1**, even though “before the first head” might suggest the other convention. The M/M/1 population starts at 0.

Memoryless proof for $X$, for nonnegative integers $m,n$ with nonzero conditioning probability:

$$
P(X>m+n\mid X>m)=\frac{q^{m+n}}{q^m}=q^n=P(X>n).
$$

For $Y$, use $P(Y\ge m+n\mid Y\ge m)=P(Y\ge n)$.

## M/M/1 queue

Sources: Queueing lecture and review; Tutorial 2 Q4; Assignment Q3/Q5.

### Model and stability

- One server, infinite waiting room, FIFO; independent Poisson arrivals at rate $\lambda$ and i.i.d. exponential service at rate $\mu$.
- State $L$ counts **waiting + in service**. Birth transitions $n\to n+1$ have rate $\lambda$; death transitions $n\to n-1$ have rate $\mu$ for $n\ge1$.
- Steady state requires $\rho=\lambda/\mu<1$. At $\lambda=\mu$, there is no normalizable stationary distribution; the formulas below do not apply.
- In stable steady state, throughput is $\lambda$, not $\mu$; the server is idle with probability $1-\rho$.
- Other notation mentioned in lecture: M/M/2 = two servers; M/M/1/K = finite system capacity; M/G/1 = general service; M/D/1 = deterministic service; G/M/1 = general inter-arrivals. The M/M/1 formulas do not automatically transfer to these models.

### Stationary probabilities and means

Local balance and normalization give $\lambda\pi_n=\mu\pi_{n+1}$, so $\pi_n=\rho^n\pi_0$ and $\pi_0=1-\rho$.

$$
\boxed{\pi_n=P(L=n)=(1-\rho)\rho^n},\qquad
P(L\ge n)=\rho^n,\qquad P(L>n)=\rho^{n+1}.
$$

$$
\boxed{\bar L=\frac\rho{1-\rho}=\frac\lambda{\mu-\lambda}},\qquad
\boxed{\bar W=\frac1{\mu-\lambda}},
$$

$$
\boxed{\bar Q=\frac{\rho^2}{1-\rho}=\frac{\lambda^2}{\mu(\mu-\lambda)}},\qquad
\boxed{\bar D=\bar W-\frac1\mu=\frac\rho{\mu-\lambda}=\rho\bar W}.
$$

Handy rearrangements:

$$
\mu=\lambda+\frac1{\bar W},\quad
\lambda=\mu-\frac1{\bar W},\quad
\rho=\frac{\bar L}{1+\bar L}=1-\frac1{\mu\bar W}
=\frac{\mu\bar D}{1+\mu\bar D}.
$$

### Empty queue versus empty system

$$
Q=\max(L-1,0),\quad P(L=0)=1-\rho,\quad
\boxed{P(Q=0)=P(L=0)+P(L=1)=1-\rho^2}.
$$

For $q\ge1$: $P(Q=q)=(1-\rho)\rho^{q+1}$ and $P(Q\ge q)=\rho^{q+1}$.

**Do not use $Q=L-1$ when $L=0$.** A queue can be empty while its server is busy.

### Short derivations to reproduce

For $|z|<1$:

$$
\sum_{n=0}^\infty z^n=\frac1{1-z},\qquad
\sum_{n=0}^\infty nz^n
=z\frac d{dz}\frac1{1-z}=\frac z{(1-z)^2},\qquad
\sum_{n=0}^\infty(n+1)z^n=\frac1{(1-z)^2}.
$$

Thus $\bar L=(1-\rho)\sum_n n\rho^n=\rho/(1-\rho)$ and $\bar Q=\bar L-\rho$ (Tutorial 2 Q4).

**Conditional sojourn time, Assignment Q3:** exponential residual service still has mean $1/\mu$.

| What an arrival sees | Conditional mean sojourn time |
|---|---|
| Empty system | $1/\mu$ |
| Empty queue, busy server | $2/\mu$ |
| $n$ other packets waiting **and a busy server** | $(n+2)/\mu$ |
| $l$ packets total in the system | $(l+1)/\mu$ |

Poisson arrivals see the stationary state proportions (the arrival-averaging fact used in the sample answer). Hence:

$$
\bar W=\sum_{l=0}^\infty\frac{l+1}\mu(1-\rho)\rho^l
=\frac{1-\rho}\mu\frac1{(1-\rho)^2}=\frac1{\mu-\lambda}.
$$

## Effective bandwidth and sharing capacity

Sources: Queueing lecture; Resource Allocation lecture; Tutorial 3 Q2/Q3.

### Delay/utilization guarantees

Effective bandwidth here means achievable throughput **subject to a quality constraint**.

| Constraint | Maximum supported arrival rate | Equivalent required service capacity |
|---|---|---|
| $\rho\le\hat\rho<1$ | $\lambda\le\hat\rho\mu$ | $\mu\ge\lambda/\hat\rho$ |
| $\bar W\le\hat W$ | $\lambda\le\mu-1/\hat W$ | $\mu\ge\lambda+1/\hat W$ |
| $\bar D\le\hat D$ | $\lambda\le\mu^2\hat D/(1+\mu\hat D)$ | $\mu\ge[\lambda+\sqrt{\lambda^2+4\lambda/\hat D}]/2$ |

The last row assumes $\hat D>0$. A sojourn target below $1/\mu$ is impossible even in the zero-load limit. If multiple bounds apply, take their minimum for $\lambda$ (or maximum for required $\mu$). Convert packets/s to bits/s by multiplying by mean bits/packet.

At fixed $\mu$, delay rises with offered load. Useful sensitivities:

$$
\frac{\partial\bar W}{\partial\lambda}=\frac1{(\mu-\lambda)^2},\qquad
\frac{\partial\bar W}{\partial\mu}=-\frac1{(\mu-\lambda)^2},\qquad
\frac{\partial^2\bar W}{\partial\lambda^2}=\frac2{(\mu-\lambda)^3}>0.
$$

### Pooled versus separated queues

Let $s_i=\mu_i-\lambda_i>0$ be spare capacity. Under the course's pooled M/M/1 model, $\mu=\sum_i\mu_i$ and $\lambda=\sum_i\lambda_i$:

$$
\bar W_{\rm pooled}=\frac1{\sum_i s_i}<\min_i\frac1{s_i}
\quad\text{for at least two queues}.
$$

The same “less than every individual delay” statement is **not guaranteed for queueing delay**: $\bar D=\bar W-1/\mu$ also changes the service-time term. A nearly unloaded separate flow can have near-zero queueing delay (Tutorial 3 Q3).

For $k$ equal partitions, each has arrival $\lambda/k$ and service $\mu/k$:

- Utilization stays $\rho$.
- Each packet's $\bar W$ and $\bar D$ become $k$ times the pooled values.
- Each partition's $\bar L,\bar Q$ equals the pooled count; **summing across partitions** gives $k$ times as many jobs.

This is the lecture's capacity-partition model of TDM; it does not add explicit slot-cycle waiting.

### Split capacity to equalize sojourn times

Equal $\bar W_i$ means equal **spare capacity**, $\mu_i-\lambda_i=s$.

$$
\boxed{\mu_i=\lambda_i+\frac{\mu-\sum_j\lambda_j}{k}},\qquad
\bar W_i=\frac{k}{\mu-\sum_j\lambda_j}.
$$

For two flows: $\mu_1=(\mu+\lambda_1-\lambda_2)/2$, $\mu_2=(\mu-\lambda_1+\lambda_2)/2$.

Tutorial 3 Q2: $(\lambda_1,\lambda_2,\mu)=(5,3,10)$ gives pooled $\bar W=0.5$ s; separate capacities $(6,4)$ give $\bar W_1=\bar W_2=1$ s.

For separate sojourn guarantees $\hat W_i$, feasibility requires $\mu\ge\sum_i(\lambda_i+1/\hat W_i)$ under the same model.

## Tandem queues and Jackson networks

Sources: Queueing lecture pp. 25–31; Tutorial 3 Q4; Tutorial 4 Q3; Assignment Q5.

### The reusable solution procedure

1. Write **one traffic equation per node**: $\lambda_i=r_i+\sum_j\lambda_jP_{ji}$.
2. Solve the simultaneous equations, including all feedback paths.
3. Check **every** node: $\rho_i=\lambda_i/\mu_i<1$.
4. Apply the stationary product form or node means, then combine for the requested network metric.

Using row vectors (as in the lecture):

$$
\boldsymbol\lambda=\mathbf r+\boldsymbol\lambda P
\quad\Rightarrow\quad
\boldsymbol\lambda=\mathbf r(I-P)^{-1}.
$$

**Burke's theorem:** a stationary M/M/1 queue's departures form a Poisson process at its arrival rate; its population at time $t$ is independent of departures before $t$. This supports tandem analysis. For networks with feedback, use the Jackson traffic equations and product-form theorem; do not assume arbitrary internal feedback streams are independent Poisson processes.

For the open, stable Jackson model with exponential single servers and probabilistic routing:

$$
P(L_1=l_1,\ldots,L_n=l_n)=\prod_i(1-\rho_i)\rho_i^{l_i},\qquad
\bar L_i=\frac{\lambda_i}{\mu_i-\lambda_i}.
$$

Let $\Lambda=\sum_i r_i$ be the total **external** arrival rate. Then:

$$
\boxed{\bar W_{\rm net}=\frac{\sum_i\bar L_i}{\Lambda}
=\sum_i\frac{v_i}{\mu_i-\lambda_i}},\qquad
v_i=\frac{\lambda_i}{\Lambda}.
$$

Here $v_i$ is the mean number of visits per external job. Summing one copy of each node's delay only works when each job visits each node exactly once. As a useful extension, total time spent waiting is $\sum_i\bar Q_i/\Lambda$.

### Feedback patterns

**Self-feedback at node 2:** external rate $\lambda$ visits node 1 once, then node 2; after each node-2 service it exits with probability $p>0$, otherwise repeats node 2.

$$
\lambda_1=\lambda,\quad \lambda_2=\lambda+(1-p)\lambda_2=\frac\lambda p,
\quad v_1=1,\ v_2=\frac1p.
$$

$$
\mu_1>\lambda,\quad\mu_2>\lambda/p,\qquad
\boxed{\bar W_{\rm net}=\frac1{\mu_1-\lambda}+\frac{1/p}{\mu_2-\lambda/p}}.
$$

**CPU–I/O cycle in lecture:** jobs start at CPU, exit CPU with probability $p$, otherwise visit I/O and return to CPU. Then $\lambda_{\rm CPU}=\lambda/p$ and $\lambda_{\rm IO}=(1-p)\lambda/p$. This is a different routing pattern from self-feedback at node 2.

**Tutorial 3 Q4:** $\lambda_1=1$, $\lambda_2=\lambda_1+\lambda_3$, $\lambda_3=0.75\lambda_2$ gives effective rates $(1,4,3)$. With $\boldsymbol\mu=(3,5,6)$:

$$
\boldsymbol\rho=(1/3,4/5,1/2),\quad
\bar L_{\rm net}=1/2+4+1=5.5,\quad
\bar W_{\rm net}=5.5/1=5.5\text{ s}.
$$

Visit-count check: $1(1/2)+4(1)+3(1/3)=5.5$ s.

### Joint queue events: Assignment Q5

For two stationary Jackson nodes with utilizations $\rho_1,\rho_2$:

| Event | Probability |
|---|---|
| Both **systems** empty | $(1-\rho_1)(1-\rho_2)$ |
| Both **waiting queues** empty | $(1-\rho_1^2)(1-\rho_2^2)$ |
| Same total population $L_1=L_2$ | $(1-\rho_1)(1-\rho_2)/(1-\rho_1\rho_2)$ |

For equal **waiting** counts, split off the zero case:

$$
\boxed{P(Q_1=Q_2)=(1-\rho_1^2)(1-\rho_2^2)
+(1-\rho_1)(1-\rho_2)\frac{(\rho_1\rho_2)^2}{1-\rho_1\rho_2}}.
$$

Reason: for $q\ge1$, $Q_1=Q_2=q$ means $L_1=L_2=q+1$. Therefore the positive-count geometric sum starts with $(\rho_1\rho_2)^2$, not $\rho_1\rho_2$. For Assignment Q5, substitute $\rho_1=\lambda/\mu_1$ and $\rho_2=\lambda/(p\mu_2)$.

---

[Chapter 1: Network Performance](01-Performance.md) | [Chapter 2: Queueing Models](02-Queueing.md) | [Chapter 3: Resource Allocation](03-Resource_Allocation.md)
