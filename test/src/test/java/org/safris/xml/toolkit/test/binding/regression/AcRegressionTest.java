/* Copyright (c) 2006 Seva Safris
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.safris.xml.toolkit.test.binding.regression;

import liberty_ac_2003_08.ac_ActivationLimit;
import liberty_ac_2003_08.ac_ActivationLimitDuration;
import liberty_ac_2003_08.ac_ActivationLimitSession;
import liberty_ac_2003_08.ac_ActivationLimitUsages;
import liberty_ac_2003_08.ac_ActivationPin;
import liberty_ac_2003_08.ac_AsymmetricDecryption;
import liberty_ac_2003_08.ac_AsymmetricKeyAgreement;
import liberty_ac_2003_08.ac_AuthenticationContextStatement;
import liberty_ac_2003_08.ac_AuthenticationMethod;
import liberty_ac_2003_08.ac_Authenticator;
import liberty_ac_2003_08.ac_AuthenticatorTransportProtocol;
import liberty_ac_2003_08.ac_DeactivationCallCenter;
import liberty_ac_2003_08.ac_DigSig;
import liberty_ac_2003_08.ac_Extension;
import liberty_ac_2003_08.ac_Generation;
import liberty_ac_2003_08.ac_GoverningAgreementRef;
import liberty_ac_2003_08.ac_GoverningAgreements;
import liberty_ac_2003_08.ac_HTTP;
import liberty_ac_2003_08.ac_IPAddress;
import liberty_ac_2003_08.ac_IPSec;
import liberty_ac_2003_08.ac_Identification;
import liberty_ac_2003_08.ac_KeyActivation;
import liberty_ac_2003_08.ac_KeySharing;
import liberty_ac_2003_08.ac_KeyStorage;
import liberty_ac_2003_08.ac_Length;
import liberty_ac_2003_08.ac_MobileNetworkEndToEndEncryption;
import liberty_ac_2003_08.ac_MobileNetworkNoEncryption;
import liberty_ac_2003_08.ac_MobileNetworkRadioEncryption;
import liberty_ac_2003_08.ac_OperationalProtection;
import liberty_ac_2003_08.ac_Password;
import liberty_ac_2003_08.ac_PhysicalVerification;
import liberty_ac_2003_08.ac_PreviousSession;
import liberty_ac_2003_08.ac_PrincipalAuthenticationMechanism;
import liberty_ac_2003_08.ac_PrivateKeyProtection;
import liberty_ac_2003_08.ac_ResumeSession;
import liberty_ac_2003_08.ac_SSL;
import liberty_ac_2003_08.ac_SecurityAudit;
import liberty_ac_2003_08.ac_SharedSecretChallengeResponse;
import liberty_ac_2003_08.ac_SharedSecretDynamicPlaintext;
import liberty_ac_2003_08.ac_Smartcard;
import liberty_ac_2003_08.ac_SwitchAudit;
import liberty_ac_2003_08.ac_TechnicalProtection;
import liberty_ac_2003_08.ac_TimeSyncToken;
import liberty_ac_2003_08.ac_Token;
import liberty_ac_2003_08.ac_WTLS;
import liberty_ac_2003_08.ac_WrittenConsent;
import liberty_ac_2003_08.ac_ZeroKnowledge;
import org.junit.Ignore;

@Ignore("Make this a real test!")
public class AcRegressionTest extends RegressionTest {
  private static final String namespaceURI = "urn:liberty:ac:2003-08";

  public static String getNamespaceURI() {
    return namespaceURI;
  }

  private static RegressionTest instance = new AcRegressionTest();

  public static void main(String[] args) {
    getAuthenticationContextStatement();
  }

  public static ac_AuthenticationContextStatement getAuthenticationContextStatement() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_AuthenticationContextStatement binding = new ac_AuthenticationContextStatement();
    binding.addac_Identification(getIdentification());
    binding.addac_TechnicalProtection(getTechnicalProtection());
    binding.addac_OperationalProtection(getOperationalProtection());
    binding.addac_AuthenticationMethod(getAuthenticationMethod());
    binding.addac_GoverningAgreements(getGoverningAgreements());
    binding.addac_Extension(getExtension());
    binding.set_ID$(new ac_AuthenticationContextStatement._ID$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_Identification getIdentification() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_Identification binding = new ac_Identification();
    binding.addac_PhysicalVerification(getPhysicalVerification());
    binding.addac_WrittenConsent(getWrittenConsent());
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());
    double random = Math.random();
    if (random < 1 / 3)
      binding.set_nym$(new ac_Identification._nym$(ac_Identification._nym$.ANONYMITY));
    else if (random < 2 / 3)
      binding.set_nym$(new ac_Identification._nym$(ac_Identification._nym$.PSEUDONYMITY));
    else
      binding.set_nym$(new ac_Identification._nym$(ac_Identification._nym$.VERINYMITY));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_PhysicalVerification getPhysicalVerification() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_PhysicalVerification binding = new ac_PhysicalVerification();
    if (Math.random() < 0.5)
      binding.set_credentialLevel$(new ac_PhysicalVerification._credentialLevel$(ac_PhysicalVerification._credentialLevel$.PRIMARY));
    else
      binding.set_credentialLevel$(new ac_PhysicalVerification._credentialLevel$(ac_PhysicalVerification._credentialLevel$.SECONDARY));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_WrittenConsent getWrittenConsent() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_WrittenConsent binding = new ac_WrittenConsent();
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_TechnicalProtection getTechnicalProtection() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_TechnicalProtection binding = new ac_TechnicalProtection();
    binding.addac_PrivateKeyProtection(getPrivateKeyProtection());
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_PrivateKeyProtection getPrivateKeyProtection() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_PrivateKeyProtection binding = new ac_PrivateKeyProtection();
    binding.addac_KeyActivation(getKeyActivation());
    binding.addac_KeyStorage(getKeyStorage());
    binding.addac_KeySharing(getKeySharing());
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_KeyActivation getKeyActivation() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_KeyActivation binding = new ac_KeyActivation();
    binding.addac_ActivationPin(getac_tivationPin());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_ActivationPin getac_tivationPin() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_ActivationPin binding = new ac_ActivationPin();
    binding.addac_ActivationLimit(getac_tivationLimit());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_ActivationLimit getac_tivationLimit() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_ActivationLimit binding = new ac_ActivationLimit();
    double random = Math.random();
    if (random < 1 / 3)
      binding.addac_ActivationLimitDuration(getac_tivationLimitDuration());
    else if (random < 2 / 3)
      binding.addac_ActivationLimitSession(new ac_ActivationLimitSession());
    else
      binding.addac_ActivationLimitUsages(getac_tivationLimitUsages());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_ActivationLimitUsages getac_tivationLimitUsages() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_ActivationLimitUsages binding = new ac_ActivationLimitUsages();
    binding.set_number$(new ac_ActivationLimitUsages._number$(getRandomInteger()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_ActivationLimitDuration getac_tivationLimitDuration() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_ActivationLimitDuration binding = new ac_ActivationLimitDuration();
    binding.set_duration$(new ac_ActivationLimitDuration._duration$(getRandomDuration()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_KeyStorage getKeyStorage() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_KeyStorage binding = new ac_KeyStorage();
    double random = Math.random();
    if (random < 1 / 5)
      binding.set_medium$(new ac_KeyStorage._medium$(ac_KeyStorage._medium$.MEMORY));
    else if (random < 2 / 5)
      binding.set_medium$(new ac_KeyStorage._medium$(ac_KeyStorage._medium$.MOBILEAUTHCARD));
    else if (random < 3 / 5)
      binding.set_medium$(new ac_KeyStorage._medium$(ac_KeyStorage._medium$.MOBILEDEVICE));
    else if (random < 4 / 5)
      binding.set_medium$(new ac_KeyStorage._medium$(ac_KeyStorage._medium$.SMARTCARD));
    else
      binding.set_medium$(new ac_KeyStorage._medium$(ac_KeyStorage._medium$.TOKEN));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_KeySharing getKeySharing() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_KeySharing binding = new ac_KeySharing();
    binding.set_sharing$(new ac_KeySharing._sharing$(getRandomBoolean()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_Password getPassword() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_Password binding = new ac_Password();
    binding.addac_Length(getLength());
    binding.addac_Generation(getGeneration());
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_Token getToken() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_Token binding = new ac_Token();
    binding.addac_TimeSyncToken(getTimeSyncToken());
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_TimeSyncToken getTimeSyncToken() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_TimeSyncToken binding = new ac_TimeSyncToken();
    if (Math.random() < 0.5)
      binding.set_DeviceType$(new ac_TimeSyncToken._DeviceType$(ac_TimeSyncToken._DeviceType$.HARDWARE));
    else
      binding.set_DeviceType$(new ac_TimeSyncToken._DeviceType$(ac_TimeSyncToken._DeviceType$.SOFTWARE));
    binding.set_SeedLength$(new ac_TimeSyncToken._SeedLength$(getRandomInteger()));
    if (Math.random() < 0.5)
      binding.set_DeviceInHand$(new ac_TimeSyncToken._DeviceInHand$(ac_TimeSyncToken._DeviceInHand$.FALSE));
    else
      binding.set_DeviceInHand$(new ac_TimeSyncToken._DeviceInHand$(ac_TimeSyncToken._DeviceInHand$.TRUE));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_Smartcard getSmartcard() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_Smartcard binding = new ac_Smartcard();
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_Length getLength() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_Length binding = new ac_Length();
    binding.set_max$(new ac_Length._max$(getRandomInteger()));
    binding.set_min$(new ac_Length._min$(getRandomInteger()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_Generation getGeneration() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_Generation binding = new ac_Generation();
    if (Math.random() < 0.5)
      binding.set_mechanism$(new ac_Generation._mechanism$(ac_Generation._mechanism$.AUTOMATIC));
    else
      binding.set_mechanism$(new ac_Generation._mechanism$(ac_Generation._mechanism$.PRINCIPALCHOSEN));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_AuthenticationMethod getAuthenticationMethod() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_AuthenticationMethod binding = new ac_AuthenticationMethod();
    binding.addac_PrincipalAuthenticationMechanism(getPrincipalAuthenticationMechanism());
    binding.addac_Authenticator(getAuthenticator());
    binding.addac_AuthenticatorTransportProtocol(getAuthenticatorTransportProtocol());
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_PrincipalAuthenticationMechanism getPrincipalAuthenticationMechanism() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_PrincipalAuthenticationMechanism binding = new ac_PrincipalAuthenticationMechanism();
    double random = Math.random();
    if (random < 1 / 3)
      binding.addac_Password(getPassword());
    else if (random < 2 / 3)
      binding.addac_Token(getToken());
    else
      binding.addac_Smartcard(getSmartcard());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_Authenticator getAuthenticator() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_Authenticator binding = new ac_Authenticator();
    double random = Math.random();
    if (random < 1 / 11)
      binding.addac_PreviousSession(getPreviousSession());
    else if (random < 2 / 11)
      binding.addac_ResumeSession(getResumeSession());
    else if (random < 3 / 11)
      binding.addac_DigSig(getDigSig());
    else if (random < 4 / 11)
      binding.addac_Password(getPassword());
    else if (random < 5 / 11)
      binding.addac_ZeroKnowledge(getZeroKnowledge());
    else if (random < 6 / 11)
      binding.addac_SharedSecretChallengeResponse(getSharedSecretChallengeResponse());
    else if (random < 7 / 11)
      binding.addac_SharedSecretDynamicPlaintext(getSharedSecretDynamicPlaintext());
    else if (random < 8 / 11)
      binding.addac_IPAddress(getIPAddress());
    else if (random < 9 / 11)
      binding.addac_AsymmetricDecryption(getAsymmetricDecryption());
    else if (random < 10 / 11)
      binding.addac_AsymmetricKeyAgreement(getAsymmetricKeyAgreement());
    else
      do
        binding.addac_Extension(getExtension());
      while(Math.random() < ADD_SEED);

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_ResumeSession getResumeSession() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_ResumeSession binding = new ac_ResumeSession();
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_PreviousSession getPreviousSession() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_PreviousSession binding = new ac_PreviousSession();
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_ZeroKnowledge getZeroKnowledge() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_ZeroKnowledge binding = new ac_ZeroKnowledge();
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_SharedSecretDynamicPlaintext getSharedSecretDynamicPlaintext() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_SharedSecretDynamicPlaintext binding = new ac_SharedSecretDynamicPlaintext();
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_SharedSecretChallengeResponse getSharedSecretChallengeResponse() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_SharedSecretChallengeResponse binding = new ac_SharedSecretChallengeResponse();
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_DigSig getDigSig() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_DigSig binding = new ac_DigSig();
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_IPAddress getIPAddress() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_IPAddress binding = new ac_IPAddress();
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_AsymmetricDecryption getAsymmetricDecryption() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_AsymmetricDecryption binding = new ac_AsymmetricDecryption();
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_AsymmetricKeyAgreement getAsymmetricKeyAgreement() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_AsymmetricKeyAgreement binding = new ac_AsymmetricKeyAgreement();
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_AuthenticatorTransportProtocol getAuthenticatorTransportProtocol() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_AuthenticatorTransportProtocol binding = new ac_AuthenticatorTransportProtocol();
    double random = Math.random();
    if (random < 1 / 8)
      binding.addac_HTTP(getHTTP());
    else if (random < 2 / 8)
      binding.addac_SSL(getSSL());
    else if (random < 3 / 8)
      binding.addac_MobileNetworkEndToEndEncryption(getMobileNetworkEndToEndEncryption());
    else if (random < 4 / 8)
      binding.addac_MobileNetworkNoEncryption(getMobileNetworkNoEncryption());
    else if (random < 5 / 8)
      binding.addac_MobileNetworkRadioEncryption(getMobileNetworkRadioEncryption());
    else if (random < 6 / 8)
      binding.addac_WTLS(getWTLS());
    else if (random < 7 / 8)
      binding.addac_IPSec(getIPSec());
    else
      do
        binding.addac_Extension(getExtension());
      while(Math.random() < ADD_SEED);

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_MobileNetworkEndToEndEncryption getMobileNetworkEndToEndEncryption() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_MobileNetworkEndToEndEncryption binding = new ac_MobileNetworkEndToEndEncryption();
    while (Math.random() < ADD_SEED) {
      binding.addac_Extension(getExtension());
    }

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_MobileNetworkNoEncryption getMobileNetworkNoEncryption() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_MobileNetworkNoEncryption binding = new ac_MobileNetworkNoEncryption();
    while (Math.random() < ADD_SEED) {
      binding.addac_Extension(getExtension());
    }

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_MobileNetworkRadioEncryption getMobileNetworkRadioEncryption() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_MobileNetworkRadioEncryption binding = new ac_MobileNetworkRadioEncryption();
    while (Math.random() < ADD_SEED) {
      binding.addac_Extension(getExtension());
    }

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_HTTP getHTTP() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_HTTP binding = new ac_HTTP();
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_IPSec getIPSec() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_IPSec binding = new ac_IPSec();
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_WTLS getWTLS() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_WTLS binding = new ac_WTLS();
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_SSL getSSL() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_SSL binding = new ac_SSL();
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_OperationalProtection getOperationalProtection() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_OperationalProtection binding = new ac_OperationalProtection();
    binding.addac_SecurityAudit(getSecurityAudit());
    binding.addac_DeactivationCallCenter(getDeactivationCallCenter());
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_SecurityAudit getSecurityAudit() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_SecurityAudit binding = new ac_SecurityAudit();
    binding.addac_SwitchAudit(getSwitchAudit());
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_SwitchAudit getSwitchAudit() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_SwitchAudit binding = new ac_SwitchAudit();
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_DeactivationCallCenter getDeactivationCallCenter() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_DeactivationCallCenter binding = new ac_DeactivationCallCenter();
    while (Math.random() < ADD_SEED)
      binding.addac_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_GoverningAgreements getGoverningAgreements() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_GoverningAgreements binding = new ac_GoverningAgreements();
    do
      binding.addac_GoverningAgreementRef(getGoverningAgreementRef());
    while(Math.random() < ADD_SEED);


    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_GoverningAgreementRef getGoverningAgreementRef() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_GoverningAgreementRef binding = new ac_GoverningAgreementRef();
    binding.set_governingAgreementRef$(new ac_GoverningAgreementRef._governingAgreementRef$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ac_Extension getExtension() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ac_Extension binding = new ac_Extension();
    do
      binding.addAny(instance.getAny());
    while(Math.random() < ADD_SEED);

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }
}