"""Regression guardrails for the local demo's websocket Origin allowlist."""
import unittest

from socket_policy import LOCAL_VITE_ORIGINS


class SocketOriginPolicyTests(unittest.TestCase):
    def test_only_local_vite_origins_are_allowed(self):
        self.assertEqual(
            set(LOCAL_VITE_ORIGINS),
            {"http://localhost:8100", "http://127.0.0.1:8100"},
        )

    def test_no_wildcard_or_remote_origin(self):
        for origin in LOCAL_VITE_ORIGINS:
            self.assertNotIn("*", origin)
            self.assertNotIn("0.0.0.0", origin)
            self.assertNotIn("https://", origin)


if __name__ == "__main__":
    unittest.main()
