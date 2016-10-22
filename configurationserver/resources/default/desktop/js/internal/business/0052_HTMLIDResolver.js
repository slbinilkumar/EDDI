function NoSuchIdException(msg) {
    this.message = msg;
}

function HTMLIDResolver() {
    /** Returns the control associated to the given html id attribute. */
    this.resolveId = function (id) {
        var refString = application.configuration.referencePrefix;

        if (id.indexOf(refString) == 0) {
            id = id.substring(refString.length, id.length);
        }

        var controlTree = application.contentBuilder.getControls();

        var recursiveControlIdResolve = function (controls) {

            for (var i = 0; i < controls.length; ++i) {
                var model = controls[i].getModel();

                if (model.hasOwnProperty('children')) {
                    try {
                        return recursiveControlIdResolve(model.children);
                    } catch (ex) {
                        if (ex instanceof NoSuchIdException) {
                            /** Continue searching. */
                            ;
                        } else {
                            /** Propagate other exception types. */
                            throw ex;
                        }
                    }
                }

                /** All IDs are of the form: prefix + id. */
                if ((model.idPrefix + model.id) === id) {
                    return controls[i];
                }
            }

            throw new NoSuchIdException('No id exists for identifier: ' + id);
        };

        return recursiveControlIdResolve(controlTree);
    }
}