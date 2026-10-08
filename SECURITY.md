# Security policy

CropLens is an **educational local-development prototype**. Its current code has not undergone an independent security audit. Passing CI or unit tests does **not** make it suitable for public Internet exposure.

## Supported usage

- Run Vue, Spring Boot and Flask on a trusted workstation bound to loopback (see [local configuration](docs/CONFIGURATION.md)).
- The paid DeepSeek relay is disabled by default. Do not enable it on an externally accessible deployment without real authentication, authorization, rate limits and budget controls.
- The image/video upload endpoints are not a hardened public file-hosting service. Do not accept untrusted uploads or personal data.
- Never commit production credentials, secret API keys, private datasets or real-user uploads.
- Dependency versions, object-model confidence and external asset licenses require ongoing verification.

## Reporting security vulnerabilities

Please use GitHub's **private vulnerability reporting** feature if this repository exposes it, or contact the repository maintainer privately using their published GitHub profile contact information. Do not publish credentials, exploit chains or user data in a public issue.

General non-security bugs can be reported through [Issues](https://github.com/FuTseYi/CropLens/issues). There is currently no guaranteed security response or patch timeline.
