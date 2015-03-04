from beaker.cache import CacheManager
from beaker.util import parse_cache_config_options
from config import CACHE


cache = CacheManager(**parse_cache_config_options(CACHE.CACHE_OPTS))
branch_cache = cache.get_cache('branch')
attribute_cache = cache.get_cache('attribute')
