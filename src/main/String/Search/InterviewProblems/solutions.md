# Cyclic rotation of a string
A string s is a cyclic rotation of a string t if s and t have the same length and s consists of a suffix of t followed by a prefix of t. For example, "winterbreak" is a cyclic rotation of "breakwinter" (and vice versa). Design a linear-time algorithm to determine whether one string is a cyclic rotation of another.
## algorithm
1. check whether s and t have the same length
2. search s in concat(2, t). if found, then s is a cyclic rotation of t; otherwise, not
## implementation
[CyclicRotation](CyclicRotation.java)

# Tandem repeat
A tandem repeat of a base string b within a string s is a substring of s consisting of at least one consecutive copy of the base string b. Given b and s, design an algorithm to find a tandem repeat of b within s of maximum length. The running time should be proportional to M + N, where M is the length of b and N is the length of s.
## algorithm
k = n / m. search concat(k, b) in s using KMP. once the state is updated, update the maximum value of consecutive b with state / m. if the state is k * m, return it directly since there can't be more than k consecutive b in s.
## implementation
[TandemRepeat](TandemRepeat.java)

# Longest panlindromic substring
## algorithm
suffix tree
## implementation