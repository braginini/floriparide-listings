import json
import logging
import traceback
from bottle import route, request
import functools
import inspect
import bottle
import sys

__author__ = 'Mike'

is_debug = True


class JsonResponse(bottle.BaseResponse):
    def __init__(self, response=None, status=None, headers=None,
                 mimetype=None, content_type=None, direct_passthrough=False):
        response = json.dumps(response, ensure_ascii=False)
        more_headers = {'Content-Type': 'application/json;charset=UTF8',
                        'Access-Control-Allow-Origin': '*',
                        'Access-Control-Allow-Methods': 'GET,POST,PUT,DELETE',
                        'Access-Control-Allow-Headers': '*'}
        super(JsonResponse, self).__init__(response, status, headers, **more_headers)

    @classmethod
    def force_type(cls, response, environ=None):
        return JsonResponse(response)


def validate(**types):
    def decorate(f):
        farg, _, _, def_params = inspect.getargspec(f)
        if def_params is None:
            def_params = []
        farg = farg[:len(farg) - len(def_params)]

        param_info = [(par, ptype, par in farg) for par, ptype in types.items()]

        @functools.wraps(f)
        def wrapper(*args, **kargs):
            getparam = request.GET.get
            for par, ptype, required in param_info:
                value = getparam(par)
                if not value:  # None or empty str
                    if required:
                        error = "%s() requires the parameter %s" % (wrapper.__name__, par)
                        raise TypeError(error)
                    continue
                try:
                    kargs[par] = ptype(value)
                except:
                    error = "Cannot convert parameter %s to %s" % (par, ptype.__name__)
                    raise ValueError(error)

            return f(*args, **kargs)

        return wrapper

    return decorate


def create_response(data):
    if isinstance(data, bottle.BaseResponse):
        return data

    status = 200

    if isinstance(data, BaseException):
        # app.log_exception(data)
        status = 500
        response_error = dict(code=500, message='Internal error')
        if is_debug:
            response_error['message'] = str(data)
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