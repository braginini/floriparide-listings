import json
import logging
import traceback
from bottle import route, request, HTTPError
import functools
import inspect
import bottle
import sys

__author__ = 'Mike'

is_debug = True


def validate(**types):
    def decorate(f):
        farg, _, _, def_params = inspect.getargspec(f)
        if def_params is None:
            def_params = []
        farg = farg[:len(farg) - len(def_params)]

        param_info = [(par, ptype, par in farg) for par, ptype in types.items()]

        @functools.wraps(f)
        def wrapper(*args, **kargs):
            getparam = request.GET.getunicode
            for par, ptype, required in param_info:
                value = getparam(par)
                if not value:  # None or empty str
                    if required:
                        error = "Bad request - %s() requires  parameter %s" % (wrapper.__name__, par)
                        raise HTTPError(body=error, status=400)
                    continue
                try:
                    kargs[par] = ptype(value)
                except:
                    error = "Bad request - Cannot convert parameter %s to %s" % (par, ptype.__name__)
                    raise HTTPError(body=error, status=400)

            return f(*args, **kargs)

        return wrapper

    return decorate


def create_response(data):
    status = 200

    if isinstance(data, (BaseException, bottle.HTTPError)):
        # app.log_exception(data)
        status = 500
        response_error = dict(code=status, message='Internal error')
        if isinstance(data, bottle.HTTPError):
            status = data.status_code
            response_error = dict(code=status, message=data.body)

        if is_debug:
            tb = sys.exc_info()[2]
            if tb:
                response_error['trace'] = traceback.format_tb(tb)

        result = dict(success=False, error=response_error)
    else:
        result = dict(success=True, result=data)
    # response = JsonResponse(result, status)
    bottle.response.headers['Content-Type'] = 'application/json;charset=UTF8'
    bottle.response.status = status
    return json.dumps(result, ensure_ascii=False)


def json_response(fn):
    @functools.wraps(fn)
    def decorator(*args, **kwargs):
        try:
            response = fn(*args, **kwargs)
        except BaseException as e:
            logging.exception(e)
            response = e
        return create_response(response)

    return decorator


def enable_cors(fn):
    def _enable_cors(*args, **kwargs):
        # set CORS headers
        bottle.response.headers['Access-Control-Allow-Origin'] = '*'
        bottle.response.headers['Access-Control-Allow-Methods'] = 'GET, POST, PUT, OPTIONS'
        bottle.response.headers[
            'Access-Control-Allow-Headers'] = 'Origin, Accept, Content-Type, X-Requested-With, X-CSRF-Token'

        if bottle.request.method != 'OPTIONS':
            # actual request; reply with the actual response
            return fn(*args, **kwargs)

    return _enable_cors