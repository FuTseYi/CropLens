"""Allowed browser origins for CropLens' localhost-only Socket.IO demo.

This is origin-based defense-in-depth, not authentication. Keep the
inference server on loopback; never treat this as a public access policy.
"""

LOCAL_VITE_ORIGINS = (
    "http://localhost:8100",
    "http://127.0.0.1:8100",
)
