# Performance — Answer Key

[Questions](../01-Performance.md) · [Practice index](../README.md)

## MCQs

| Question | Answer | Explanation |
|---|---|---|
| P1 | **B** | Throughput is 12 Mbps; utilization is $12/20=0.6$. Averages do not exclude bursts. |
| P2 | **D** | 12,000 bits gives transmission times $(1,2,4)$ ms. Propagation times are $(1,0.5,1.5)$ ms. Router delay is $2(0.2+0.5)=1.4$ ms. Total: $7+3+1.4=11.4$ ms. |
| P3 | **A** | Propagation alone is $400{,}000/(2\times10^8)=0.002$ s = 2 ms. Increasing bandwidth does not remove it. |
| P4 | **C** | At most 3 active users fit. For $X\sim\mathrm{Binomial}(5,0.2)$, $P(X>3)=5(0.2)^4(0.8)+(0.2)^5=0.00672$. A counts 3 active users too; B counts only 5. |

## P5

- (a) **0.6**.
- (b) **150 ms**.
- (c) **155 ms**.
- (d) **18.6 jobs**.
- (e) **False**.

$\rho=120(0.005)=0.6$. Queue Little's law gives $\bar D=18/120=0.15$ s. Add service: $\bar W=0.155$ s. Then $\bar L=120(0.155)=18.6$. These identities do not require M/M/1 assumptions.

## P6

- (a) **175 bytes**.
- (b) **96.875 bytes**.
- (c) **0.96875 ms**.
- (d) **0.775**.
- (e) **50 ms**.
- (f) **49.03125 ms**.

$E[B_A]=0.25(100)+0.75(200)=175$. Total arrivals are 800 packets/s. Weight the packet size by arrival rates:

$$
E[B]=\frac{300(175)+500(50)}{800}=96.875,\qquad
E[S]=\frac{96.875}{100{,}000}=0.00096875\text{ s}.
$$

$\rho=800E[S]=0.775$, $\bar W=40/800=0.05$ s, and $\bar D=\bar W-E[S]$. Service times here are a discrete mixture, not exponential; the M/M/1 delay formula is not applicable.

## P7

- (a) **200 requests/s**.
- (b) **16 requests**.
- (c) **200 requests/s**.

The admitted rate is $250(0.8)=200$. Little's law for admitted requests gives $200(0.08)=16$. Rejected requests do not spend 0.08 s in that subsystem.

## P8

- (a) **$\bar L-\rho$**.
- (b) **$(\bar L-\rho)/\lambda$**.
- (c) **$\rho$**.
- (d) **False**.

The number in service is a 0/1 indicator with mean $\rho$, so $\bar L=\bar Q+\rho$. Apply $\bar Q=\lambda\bar D$. Subtracting 1 instead of $\rho$ incorrectly treats the server as always busy.
