import json
import logging
import traceback
import functools
import inspect
import sys
from bottle import request
from tornado.web import HTTPError

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
            getparam = request.GET.get
            for par, ptype, required in param_info:
                value = getparam(par)
                if not value:  # None or empty str
                    if required:
                        error = "%s() requires the parameter %s" % (wrapper.__name__, par)
                        raise HTTPError(error)
                    continue
                try:
                    kargs[par] = ptype(value)
                except:
                    error = "Cannot convert parameter %s to %s" % (par, ptype.__name__)
                    raise HTTPError(error)

            return f(*args, **kargs)

        return wrapper

    return decorate


def create_response(data, request):
    status = 200

    if isinstance(data, (BaseException, HTTPError)):
        # app.log_exception(data)
        status = 500
        response_error = dict(code=status, message='Internal error')
        if isinstance(data, HTTPError):
            status = data.status_code
            response_error = dict(code=status, message=data.log_message)

        if is_debug:
            tb = sys.exc_info()[2]
            if tb:
                response_error['trace'] = traceback.format_tb(tb)

        result = dict(success=False, error=response_error)
    else:
        result = dict(success=True, result=data)
    # response = JsonResponse(result, status)
    request.set_header('Content-Type', 'application/json;charset=UTF8')
    request.set_status(status)
    return json.dumps(result, ensure_ascii=False)


def json_response(fn):
    @functools.wraps(fn)
    def decorator(*args, **kwargs):
        try:
            #result is wrapped to Future tha is why we need to get the result() and exception()
            response = fn(*args, **kwargs).result()
            if not response:
                response = fn(*args, **kwargs).exception()
        except BaseException as e:
            logging.exception(e)
            response = e
        args[0].write(create_response(response, args[0]))

    return decorator


def enable_cors(fn):
    def _enable_cors(*args, **kwargs):
        # set CORS headers
        args[0].set_header('Access-Control-Allow-Origin', '*')
        args[0].set_header('Access-Control-Allow-Methods', 'GET, POST, PUT, OPTIONS')
        args[0].set_header('Access-Control-Allow-Headers',
                           'Origin, Accept, Content-Type, X-Requested-With, X-CSRF-Token')

        # if bottle.request.method != 'OPTIONS':
        #     # actual request; reply with the actual response
        return fn(*args, **kwargs)

    return _enable_cors