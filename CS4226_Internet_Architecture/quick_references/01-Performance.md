# Chapter 1: Network Performance

[Reference index, notation and formula lookup](README.md)

## Network performance and Little's law

Sources: Performance lecture; Tutorial 1 Q2/Q4; Assignment Q4.

### Delay, bandwidth and throughput

$$
d_{\rm tx}=\frac bR,\qquad d_{\rm prop}=\frac\ell v,
\qquad d_{\rm end-to-end}=\sum_{\rm hops}(d_{\rm proc}+d_{\rm queue}+d_{\rm tx}+d_{\rm prop}).
$$

Derived forms: $R=b/d_{\rm tx}$, $b=Rd_{\rm tx}$, $\ell=vd_{\rm prop}$.

- **Capacity/bandwidth:** physical processing/transmission limit. **Throughput:** actual completed data per unit time. **Utilization:** fraction of capacity/time used.
- **Store-and-forward:** the whole packet must arrive before transmission on the next link. For one packet over $h$ equal-rate links, ignoring other delays: $d=hb/R$.
- Example from the lecture: $b=7.5$ Mbits, $R=1.5$ Mbps gives **5 s per link**, hence **15 s over three links**. The slide's 15 s refers to the three-link total.
- **Packet switching/statistical multiplexing:** share capacity on demand; efficient for bursty traffic, but bursts cause queues and potentially loss with finite buffers.
- **Circuit switching/TDM:** reserve shares; predictable allocation but unused shares can be wasted.
- Queues form because arrivals can temporarily exceed service, even when the long-run mean arrival rate is below capacity.

### Little's law: general, not just M/M/1

$$
\boxed{\bar L=\lambda\bar W},\qquad
\bar W=\frac{\bar L}{\lambda},\qquad
\lambda=\frac{\bar L}{\bar W},\qquad
\boxed{\bar Q=\lambda\bar D}.
$$

Use stable long-run averages and consistent boundaries. If jobs can be rejected, use the admitted rate when measuring admitted jobs' time in the system. Poisson arrivals and exponential service are **not** required.

For a stable, work-conserving single server:

$$
\rho=\lambda E[S]=\frac\lambda\mu,\qquad
\bar L=\bar Q+\rho,\qquad
\bar W=\bar D+E[S],\qquad
\boxed{\bar D=\frac{\bar L-\rho}{\lambda}}.
$$

Examples: 1.5 students/year × 6 years = 9 students; mean waiting queue of 5 packets at 100 packets/s gives $\bar D=0.05$ s.

### Mixed packet sizes: Assignment Q4

Let stream $i$ have rate $\lambda_i$ and mean packet size $E[B_i]$, and let $R$ use matching size units/s.

$$
\lambda=\sum_i\lambda_i,\quad
P(\text{packet is type }i)=\frac{\lambda_i}{\lambda},\quad
E[S]=\sum_i\frac{\lambda_i}{\lambda}\frac{E[B_i]}R,
\quad
\boxed{\rho=\frac{\sum_i\lambda_iE[B_i]}R}.
$$

**Weight by packet arrival rates**, not by equal shares per stream. Also $\mu=1/E[S]$, not $E[1/S]$.

Assignment values: TCP 200 packets/s with mean size $(50+100)/2=75$ bytes; UDP 500 packets/s, 30 bytes each; $R=50{,}000$ bytes/s.

$$
\rho=\frac{200(75)+500(30)}{50{,}000}=0.6,\quad
E[S]=\frac{0.6}{700}\text{ s},\quad
\bar D=\frac{1000-0.6}{700}\approx1.427714\text{ s}.
$$

The given service distribution is **not exponential**. Use Little's law and utilization, not the M/M/1 delay formula.

---

[Chapter 1: Network Performance](01-Performance.md) | [Chapter 2: Queueing Models](02-Queueing.md) | [Chapter 3: Resource Allocation](03-Resource_Allocation.md)
