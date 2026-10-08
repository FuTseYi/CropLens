"""Validation boundaries for CropLens' local YOLO inference service.

The demo accepts only model files from the bundled weights directory and
media URLs returned by the *local* Spring Boot upload endpoint.
"""
import re
from pathlib import Path
from urllib.parse import unquote, urlsplit


_MODEL_FILENAME = re.compile(r"^[A-Za-z0-9_.-]+\.pt$")


def resolve_weight_file(name, weights_dir="./weights"):
    """Return an existing .pt checkpoint inside the checked-in weights dir."""
    if not isinstance(name, str) or not _MODEL_FILENAME.fullmatch(name):
        raise ValueError("Invalid model filename")
    base = Path(weights_dir).resolve()
    selected = (base / name).resolve()
    if selected.parent != base or not selected.is_file():
        raise ValueError("Model file is not available")
    return str(selected)


def validate_local_media_url(url):
    """Accept media only from Spring Boot's local /files/ upload endpoint.

    A URL is not a reliable authentication mechanism; callers must keep
    both local services bound to loopback and never expose this demo publicly.
    """
    if not isinstance(url, str) or len(url) > 2048 or any(ch.isspace() for ch in url):
        raise ValueError("Invalid media URL")
    try:
        parsed = urlsplit(url)
        port = parsed.port
    except ValueError as exc:
        raise ValueError("Invalid media URL") from exc
    if (
        parsed.scheme != "http"
        or parsed.hostname not in ("localhost", "127.0.0.1")
        or port != 9999
        or parsed.username is not None
        or parsed.password is not None
        or parsed.query
        or parsed.fragment
    ):
        raise ValueError("Only the local upload endpoint is supported")
    path = unquote(parsed.path)
    filename = path[len("/files/"):] if path.startswith("/files/") else ""
    if (
        not parsed.path.startswith("/files/")
        or not filename
        or filename in (".", "..")
        or "/" in filename
        or "\\" in filename
        or "\x00" in filename
        or "%" in filename
    ):
        raise ValueError("Invalid upload file path")
    return url
