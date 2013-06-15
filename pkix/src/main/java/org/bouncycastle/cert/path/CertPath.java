package org.bouncycastle.cert.path;

import org.bouncycastle.cert.X509CertificateHolder;

public class CertPath
{
    private final X509CertificateHolder[] certificates;

    public CertPath(X509CertificateHolder[] certificates)
    {
        this.certificates = copyArray(certificates);
    }

    public X509CertificateHolder[] getCertificates()
    {
        return copyArray(certificates);
    }

    public CertPathValidationResult validate(CertPathValidation[] ruleSet)
    {
        CertPathValidationContext context = new CertPathValidationContext();

        for (int i = 0; i != ruleSet.length; i++)
        {
            for (int j = certificates.length - 1; j >= 0; j--)
            {
                try
                {
                    ruleSet[i].validate(context, certificates[j]);
                }
                catch (CertPathValidationException e)
                {
                    return new CertPathValidationResult(j, i, e);
                }
            }
        }

        return new CertPathValidationResult();
    }

    public CertPathValidationResult evaluate(CertPathValidation[] ruleSet)
    {
        CertPathValidationContext context = new CertPathValidationContext();
        CertPathValidationResultBuilder builder = new CertPathValidationResultBuilder();

        for (int i = 0; i != ruleSet.length; i++)
        {
            for (int j = certificates.length - 1; j >= 0; j--)
            {
                try
                {
                    ruleSet[i].validate(context, certificates[j]);
                }
                catch (CertPathValidationException e)
                {
                   builder.addException(e);
                }
            }
        }

        return builder.build();
    }

    private X509CertificateHolder[] copyArray(X509CertificateHolder[] array)
    {
        X509CertificateHolder[] rv = new X509CertificateHolder[array.length];

        System.arraycopy(array, 0, rv, 0, rv.length);

        return rv;
    }
}