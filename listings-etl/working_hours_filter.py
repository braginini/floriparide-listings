source.get('schedule') is not None and _source.get('schedule').get(day) is not None and len([r for r in _source.get('schedule').get(day) if r['from'] < hour < r['to']]) > 0

