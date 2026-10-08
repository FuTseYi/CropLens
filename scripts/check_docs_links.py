"""Validate repository-local links in Markdown documentation.

External destinations and fragment-only links are intentionally excluded;
this checker verifies local filesystem destinations, not external URLs.
"""
from pathlib import Path
import re
from urllib.parse import unquote, urlsplit

ROOT = Path(__file__).resolve().parents[1]
LINK = re.compile(r"!?(?:\[[^\]\n]*\])\(([^)\n]+)\)")


def check_links():
    problems = []
    inspected = 0
    for md in sorted(ROOT.rglob("*.md")):
        for match in LINK.finditer(md.read_text(encoding="utf-8")):
            target = match.group(1).strip()
            if target.startswith("<") and ">" in target:
                target = target[1:target.index(">")]
            else:
                target = target.split(maxsplit=1)[0]
            if not target or target.startswith("#"):
                continue
            parsed = urlsplit(target)
            if parsed.scheme or parsed.netloc:
                continue
            clean = unquote(parsed.path)
            if not clean:
                continue
            inspected += 1
            destination = (ROOT / clean.lstrip("/")) if clean.startswith("/") else (md.parent / clean)
            destination = destination.resolve()
            try:
                destination.relative_to(ROOT)
            except ValueError:
                problems.append("{}: local link escapes repository: {}".format(md.relative_to(ROOT), target))
                continue
            if not destination.exists():
                problems.append("{}: missing local target: {}".format(md.relative_to(ROOT), target))
    print("Checked {} local Markdown links".format(inspected))
    if problems:
        raise SystemExit("\n".join(problems))
    print("All checked local links resolve.")


if __name__ == "__main__":
    check_links()
